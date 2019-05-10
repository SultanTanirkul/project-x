package com.project.group18.limberup;

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

        EditText m_Username_EditText = findViewById(R.id.username_params);
        EditText m_Password_EditText = findViewById(R.id.password_params);
        EditText m_Email_EditText = findViewById(R.id.email_params);
        EditText m_Bio_EditText = findViewById(R.id.bio_params);
        EditText m_Interests_EditText = findViewById(R.id.interests_params);

        m_SignUp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
                Map<String, String> userRegisterParams = new HashMap<>();

                userRegisterParams.put("username", m_Username_EditText.getText().toString());
                Log.v("Test: ", m_Username_EditText.getText().toString());
                userRegisterParams.put("password", m_Password_EditText.getText().toString());
                Log.v("Test: ", m_Password_EditText.getText().toString());

                userRegisterParams.put("email", m_Email_EditText.getText().toString());
                userRegisterParams.put("bio", m_Bio_EditText.getText().toString());
                userRegisterParams.put("interests", m_Interests_EditText.getText().toString());
                serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/create", userRegisterParams ,(s) -> isRegistered(s)));
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
