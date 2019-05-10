package com.project.group18.limberup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityCategory extends AppCompatActivity {

    // Button declaration to get to activity list
    private Button m_Football_Activity_Button   = null;
    private Button m_Basketball_Activity_Button = null;
    private Button m_Tennis_Activity_Button     = null;
    private Button m_Cricket_Activity_Button    = null;
    private Button m_Golf_Activity_Button       = null;
    private Button m_Volleyball_Activity_Button = null;
    private Button m_Swimming_Activity_Button   = null;
    private Button m_Other_Activity_Button      = null;
    private Button m_Create_Category_Button     = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        m_Football_Activity_Button = findViewById(R.id.activity_category_football);
        m_Basketball_Activity_Button = findViewById(R.id.activity_category_basketball);
        m_Tennis_Activity_Button = findViewById(R.id.activity_category_tennis);
        m_Cricket_Activity_Button = findViewById(R.id.activity_category_cricket);
        m_Golf_Activity_Button = findViewById(R.id.activity_category_golf);
        m_Volleyball_Activity_Button = findViewById(R.id.activity_category_volleyball);
        m_Swimming_Activity_Button = findViewById(R.id.activity_category_swimming);
        m_Other_Activity_Button = findViewById(R.id.activity_category_other);
        m_Create_Category_Button = findViewById(R.id.create_activity_bt);

        // Button Functionality
        buttonFunctions();
    }





    // Functions to definition of the category button
    private void buttonFunctions()
    {
        // Football Category
        m_Football_Activity_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCategory.this, ActivityEvents.class);
                startActivity(intent);
            }
        });
        // Basketball Category
        m_Basketball_Activity_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCategory.this, ActivityEvents.class);
                startActivity(intent);
            }
        });

        // Tennis Activity
        m_Tennis_Activity_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCategory.this, ActivityEvents.class);
                startActivity(intent);
            }
        });

        // Cricket Activity
        m_Cricket_Activity_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCategory.this, ActivityEvents.class);
                startActivity(intent);
            }
        });
        // Golf Activity
        m_Golf_Activity_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCategory.this, ActivityEvents.class);
                startActivity(intent);
            }
        });


        // Volleyball Activity
        m_Volleyball_Activity_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCategory.this, ActivityEvents.class);
                startActivity(intent);
            }
        });

        // Swimming Activity
        m_Swimming_Activity_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCategory.this, ActivityEvents.class);
                startActivity(intent);
            }
        });

        // Swimming Activity
        m_Other_Activity_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCategory.this, ActivityEvents.class);
                startActivity(intent);
            }
        });

        // Create Category
        m_Create_Category_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityCategory.this, CreateEvent.class);
                startActivity(intent);
            }
        });
    }


}