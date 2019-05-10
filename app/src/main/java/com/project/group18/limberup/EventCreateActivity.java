package com.project.group18.limberup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class EventCreateActivity extends AppCompatActivity {

    private ImageView m_Map_Button = null;
    public static final int RESULT_OK = 1;
    private EditText m_Location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        m_Map_Button = findViewById(R.id.create_event_map_button);
        m_Location = findViewById(R.id.create_event_location);

        m_Map_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventCreateActivity.this, MapActivity.class);
                startActivityForResult(intent, RESULT_OK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            String latLng =data.getStringExtra("LatLng");
            m_Location.setText(latLng);
        }
    }
}
