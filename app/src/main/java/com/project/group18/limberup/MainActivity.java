package com.project.group18.limberup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Declaration of View
    private TextView m_RegistrationLink = null;
    private TextView m_ForgetPassLink = null;
    private EditText m_Username_EditText = null;
    private EditText m_Password_EditText = null;
    private Button m_Login_Button = null;


    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization of View Elements.
        m_Login_Button = findViewById(R.id.login_button);
        m_Username_EditText = findViewById(R.id.username_params);
        m_Password_EditText = findViewById(R.id.password_params);
        m_RegistrationLink = findViewById(R.id.link_register);
        m_ForgetPassLink = findViewById(R.id.link_forget_pass);


        // Handle logged in user and signout
        if (getIntent() != null) {
            Intent intent = getIntent();
            String action = intent.getStringExtra(Dashboard.EXTRA_MESSAGE);
            if (action != null) {
                signOut();
            } else if (checkIfLogged()) {
                loggedIn();
            }
        }

        // Some functionality for Login Button
        m_Login_Button.setOnClickListener((View v) -> {
            Map<String, String> userLoginParams = new HashMap<>();
            userLoginParams.put("username", m_Username_EditText.getText().toString());
            userLoginParams.put("password", m_Password_EditText.getText().toString());

            dialog = ProgressDialog.show(MainActivity.this, "",
                    "Loading. Please wait...", true);
            dialog.show();

            SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", m_Username_EditText.getText().toString());
            editor.putString("password", (m_Password_EditText.getText().toString()));
            editor.apply();
            ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
            serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/authenticate", userLoginParams, (s) -> login(s)));

            dialog.hide();
        });

        // Some functionality for registration link/text.
        m_RegistrationLink.setOnClickListener((View v) -> {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
        
    }

    /**
     * Function for validation if user has been logged in to the system.
     *
     * @return boolean meaning success of the outcome.
     */
    private boolean checkIfLogged() {
        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);
        String password = sharedPref.getString("password", null);
        String token = sharedPref.getString("token", null);
        if (token != null) {

            ServerOp checkTokenReq = ServerOp.getInstance(getApplicationContext());
            Map<String, String> authPrams = new HashMap<>();
            authPrams.put("token", token);
            checkTokenReq.addToRequestQueue(checkTokenReq.postRequest("https://limberup.herokuapp.com/checktoken", authPrams, (s) -> checkToken(s)));
            if (password != null && username != null) {
                Map<String, String> userLoginParams = new HashMap<>();
                userLoginParams.put("username", username);
                userLoginParams.put("password", password);
                ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
                serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/authenticate", userLoginParams, (s) -> login(s)));
            }
        }

        return false;

    }


    /**
     * Method to show login items
     */
    private void loginLayout() {
        m_Username_EditText.setVisibility(View.VISIBLE);
        m_Password_EditText.setVisibility(View.VISIBLE);
        m_RegistrationLink.setVisibility(View.VISIBLE);
        m_Login_Button.setVisibility(View.VISIBLE);
        m_ForgetPassLink.setVisibility(View.VISIBLE);
    }

    private void checkToken(String s){
        if (s =="1"){
            loggedIn();
        }
    }

    /**
     * Login method
     */
    private boolean login(String s) {
        try {
            JSONObject response = new JSONObject(s);
            if(response.getBoolean("success")){
                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("token", response.getString("token"));
                editor.apply();
                loggedIn();
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Move to the dashboard layout
     */
    private void loggedIn() {
        Intent intent = new Intent(MainActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    /**
     * Functionality for sign out button.
     */
    private boolean signOut() {
        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPref.edit().remove("username").apply();
        sharedPref.edit().remove("password").apply();
        sharedPref.edit().remove("token").apply();
        loginLayout();
        Toast.makeText(this, "Signed Out successfully", Toast.LENGTH_LONG).show();
        return true;
    }


}
