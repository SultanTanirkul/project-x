package com.example.group18.buddybuddygym;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = (TextView) findViewById(R.id.text);
        ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
        text.setText("hello");
        StringRequest newUser = serverOp.getRequest("http://10.0.2.2:9000/createUser", s -> text.setText(s));
        serverOp.addToRequestQueue(newUser);


    }
}
