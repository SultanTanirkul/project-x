package com.project.group18.limberup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityCategory extends AppCompatActivity {
    private Button m_Open_Activity_Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        m_Open_Activity_Button = findViewById(R.id.activity_category_football);

        m_Open_Activity_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCategory.this, ActivityEvents.class);
                startActivity(intent);
            }
        });
    }
}
