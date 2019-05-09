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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Declaration of View
    private TextView m_RegistrationLink  = null;
    private TextView m_ForgetPassLink    = null;
    private EditText m_Username_EditText = null;
    private EditText m_Password_EditText = null;
    private Button   m_Login_Button      = null;
    private Button   m_LogOut_Button     = null;
    private Button   m_Profile_Button    = null;
    private Button   m_Activities_Button    = null;

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
        m_LogOut_Button = findViewById(R.id.sign_out_button);
        m_Profile_Button = findViewById(R.id.profile_button);
        m_Activities_Button = findViewById(R.id.activity_search_button);
        changeLayout(View.GONE);
        // Validation if user is signed in.
        checkIfLogged();

        // Some functionality for Login Button
        m_Login_Button.setOnClickListener((View v)->{
            ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
            Map<String, String> userLoginParams = new HashMap<>();
            userLoginParams.put("username", m_Username_EditText.getText().toString());
            userLoginParams.put("password", Security.hashPassword(m_Password_EditText.getText().toString()));
            dialog = ProgressDialog.show(MainActivity.this, "",
                    "Loading. Please wait...", true);
            dialog.show();
            serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/authenticate", userLoginParams ,(s) -> {
                try {
                    isSignIn(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.hide();
                }
            }));
            dialog.hide();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(m_Password_EditText.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(m_Username_EditText.getWindowToken(), 0);
        });

        // Some functionality for registration link/text.
        m_RegistrationLink.setOnClickListener((View v) ->
        {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        // Some functionality for Log out button.
        m_LogOut_Button.setOnClickListener((View v) -> {
                    signOut();
        });

        m_Profile_Button.setOnClickListener((View v) -> {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        m_Activities_Button.setOnClickListener((View v) ->{
            Intent intent = new Intent(MainActivity.this, ActivityCategory.class);
            startActivity(intent);
        });
    }

    /**
     * Signing in to the system for the first time to save password and username in
     * shared preferences.
     *
     * @param serverResponse
     * @return boolean meaning success of the function.
     * @throws JSONException
     */
    private boolean isSignIn(String serverResponse) throws JSONException {

        JSONObject jObj = new JSONObject(serverResponse);
        boolean isLogged = jObj.getBoolean("success");
        String token = jObj.getString("token");
        if(isLogged){
            SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", m_Username_EditText.getText().toString());
            editor.putString("password", Security.hashPassword(m_Password_EditText.getText().toString()));
            editor.apply();

            Log.v("ClientError", m_Username_EditText.getText().toString() + " " + m_Password_EditText.getText().toString());

            Log.v("Server", serverResponse);
            changeLayout(View.GONE);
            m_Password_EditText.setText(null);
            m_Username_EditText.setText(null);
            return true;
        }
        Log.v("Server: ", serverResponse);
        return false;
    }

    /**
     * Function for validation if user has been logged in to the system.
     * @return boolean meaning success of the outcome.
     */
    private boolean checkIfLogged(){
        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);
        String password = sharedPref.getString("password", null);

        if(password == null || username == null){
            Log.v("Client", "User is not logged in because password or username is null.");
            Toast.makeText(this,"Please, Create a new account or Log In", Toast.LENGTH_LONG).show();
            return false;
        }else{
            ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
            Map<String, String> userLoginParams = new HashMap<>();
            userLoginParams.put("username", username);
            userLoginParams.put("password", password);
            Log.v("Client", username + " " + password);
            serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/authenticate", userLoginParams , (s) -> Log.v("Server", s)));
            changeLayout(View.GONE);
            Toast.makeText(this,"Thank you for using LimberUP", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    /**
     * Code for changing layout.
     * @param visibilityType
     */
    private void changeLayout(int visibilityType){
        m_Username_EditText.setVisibility(visibilityType);
        m_Password_EditText.setVisibility(visibilityType);
        m_RegistrationLink.setVisibility(visibilityType);
        m_Login_Button.setVisibility(visibilityType);
        m_ForgetPassLink.setVisibility(visibilityType);
        if(visibilityType == View.VISIBLE){
            m_LogOut_Button.setVisibility(View.GONE);
            m_Profile_Button.setVisibility(View.GONE);
            m_Activities_Button.setVisibility(View.GONE);
        }else{
            m_LogOut_Button.setVisibility(View.VISIBLE);
            m_Profile_Button.setVisibility(View.VISIBLE);
            m_Activities_Button.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Functionality for sign out button.
     * @return
     */
    private boolean signOut() {
        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sharedPref.edit().remove("username").apply();
        sharedPref.edit().remove("password").apply();
        changeLayout(View.VISIBLE);
        Toast.makeText(this,"Signed Out successfully", Toast.LENGTH_LONG).show();
        return true;
    }
}
