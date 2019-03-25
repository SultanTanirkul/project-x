package com.example.group18.buddybuddygym;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_test_page);
        TextView text = (TextView) findViewById(R.id.text);
        ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
        text.setText("hello");
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "newUser");
        params.put("password", "helloworld");
        StringRequest login = serverOp.postRequest("http://10.0.2.2:9000/createUser", params, s -> text.setText(s));
        serverOp.addToRequestQueue(login);
        StringRequest viewRestricted = serverOp.getRequest("http://10.0.2.2:9000/user", s -> text.setText(s));
        serverOp.addToRequestQueue(viewRestricted);


    }
}
