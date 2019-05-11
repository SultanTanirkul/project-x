package com.project.group18.limberup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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



        // Handle previously logged in user
        if (checkIfLogged()) {
            Log.i("---->", "Checking if stored user exists!");
            loggedIn();
        }

        // Some functionality for Login Button
        m_Login_Button.setOnClickListener((View v) -> {
            Log.i("---->", "Login button pressed");
            dialog = ProgressDialog.show(MainActivity.this, "",
                    "Loading. Please wait...", true);

            Map<String, String> userLoginParams = new HashMap<>();
            userLoginParams.put("username", m_Username_EditText.getText().toString());
            userLoginParams.put("password", m_Password_EditText.getText().toString());

            SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", m_Username_EditText.getText().toString());
            editor.putString("password", (m_Password_EditText.getText().toString()));
            editor.apply();

            ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
            serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/authenticate", userLoginParams, (s) -> {
                Log.i("---->", "Response: " + s);
                dialog.hide();
                login(s);
            }));

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
            Log.i("---->", "Token exists");
            ServerOp checkTokenReq = ServerOp.getInstance(getApplicationContext());
            Map<String, String> authPrams = new HashMap<>();
            authPrams.put("token", token);
            checkTokenReq.addToRequestQueue(checkTokenReq.postRequest("https://limberup.herokuapp.com/api/verify", authPrams, (s) -> {
                if (Integer.parseInt(s) == 200){
                    Log.i("---->", "Token authenticated: " + s);
                    loggedIn();
                }
                else{
                    Log.i("---->", "Token not authenticated: " + s);
                }
            }));
            Log.i("---->", "Checking stored login");
            if (password != null && username != null) {
                Log.i("---->", "Checking stored login");
                Map<String, String> userLoginParams = new HashMap<>();
                userLoginParams.put("username", username);
                userLoginParams.put("password", password);
                Log.i("---->", "User: " + username + " Pass: " +password);
                ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
                serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/authenticate", userLoginParams, (s) -> login(s)));
            }
        }
        Log.i("---->", "User not auto logged in");
        return false;

    }

    private void checkToken(String s){
        Log.i("---->",  s);
        if (Integer.parseInt(s) == 200){
            Log.i("---->", "Token authenticated: " + s);
            loggedIn();
        }
        else{
            Log.i("---->", "Token not authenticated: " + s);
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
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
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
        Toast.makeText(this, "Signed Out successfully", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    protected void onPause(){
        super.onPause();
        dialog.dismiss();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        dialog.dismiss();
    }



}
