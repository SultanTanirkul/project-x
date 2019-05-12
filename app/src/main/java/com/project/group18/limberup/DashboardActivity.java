package com.project.group18.limberup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {

    private Button m_LogOut_Button      = null;
    private Button m_Event_Invitations_Button      = null;
    private Button m_Event_Recommendations_Button      = null;
    private Button m_Dashboard_Explore_Button = null;
    private Button m_Dashboard_Feed_Button = null;

    public static final String EXTRA_MESSAGE = "com.project.group18.limberup.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SharedPreferences sharedPref = DashboardActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // Initialization of View Elements.
        m_LogOut_Button = findViewById(R.id.sign_out_button);
        m_Event_Invitations_Button = findViewById(R.id.event_invitations);
        m_Event_Recommendations_Button = findViewById(R.id.event_recommendations);
        m_Dashboard_Explore_Button = findViewById(R.id.dashboard_explore_button);
        m_Dashboard_Feed_Button = findViewById(R.id.dashboard_feed_button);

        // Some functionality for Log out button.
        m_LogOut_Button.setOnClickListener((View v) -> {
            sharedPref.edit().clear().commit();
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        m_Event_Invitations_Button.setOnClickListener((View v) -> {
            Intent intent = new Intent(DashboardActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        m_Event_Recommendations_Button.setOnClickListener((View v) ->{
            Intent intent = new Intent(DashboardActivity.this, ActivityCategory.class);
            startActivity(intent);
        });

        m_Dashboard_Explore_Button.setOnClickListener((View v) ->{
            Intent intent = new Intent (DashboardActivity.this, ExploreActivity.class);
            startActivity(intent);
        });

        m_Dashboard_Feed_Button.setOnClickListener((View v ) ->{
            Intent intent = new Intent(DashboardActivity.this, FeedActivity.class);
            startActivity(intent);
        });
    }


}
