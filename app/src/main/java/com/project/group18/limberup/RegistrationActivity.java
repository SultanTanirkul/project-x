package com.project.group18.limberup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button m_SignUp_Button = findViewById(R.id.registration_button);
        Button m_SignOut_Button = findViewById(R.id.signup_back_button);
        EditText m_Username_EditText = findViewById(R.id.username_params);
        EditText m_Password_EditText = findViewById(R.id.password_params);
        EditText m_Email_EditText = findViewById(R.id.email_params);
        EditText m_Bio_EditText = findViewById(R.id.bio_params);
        EditText m_Interests_EditText = findViewById(R.id.interests_params);
        EditText m_dob = findViewById(R.id.dob_text);
        Button m_PrivacyAndPolicy = findViewById(R.id.privacy_reg_btn);

        m_PrivacyAndPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });



        m_SignOut_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        m_SignUp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
                Map<String, String> userRegisterParams = new HashMap<>();

                userRegisterParams.put("username", m_Username_EditText.getText().toString());
                Log.v("User: ", m_Username_EditText.getText().toString());
                userRegisterParams.put("password", m_Password_EditText.getText().toString());
                Log.v("Pass: ", m_Password_EditText.getText().toString());
                userRegisterParams.put("dob", m_dob.getText().toString());
                Log.v("DOB: ", m_Password_EditText.getText().toString());

                userRegisterParams.put("email", m_Email_EditText.getText().toString());
                userRegisterParams.put("bio", m_Bio_EditText.getText().toString());
                userRegisterParams.put("interests", m_Interests_EditText.getText().toString());
                serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/create", userRegisterParams ,(s) -> isRegistered(s)));

                SharedPreferences sharedPref = RegistrationActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("offline", "0");
                editor.putString("name", m_Bio_EditText.getText().toString());
                editor.putString("bio",m_Bio_EditText.getText().toString());
                editor.apply();
            }
        });

    }


    //
    public boolean isRegistered(String serverResponse)
    {
        Log.v("Server", serverResponse);
        if(serverResponse.equals("User has been added")){
            finish();
            return true;
        }
        else{
            Log.e("Server", serverResponse);

            return false;
        }
    }
}
