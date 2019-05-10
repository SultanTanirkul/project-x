package com.project.group18.limberup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class EventCreateActivity extends AppCompatActivity {

    private ImageView m_Map_Button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        m_Map_Button = findViewById(R.id.create_event_map_button);


        m_Map_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventCreateActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
}
