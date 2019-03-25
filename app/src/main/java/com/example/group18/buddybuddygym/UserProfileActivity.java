package com.example.group18.buddybuddygym;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserProfileActivity extends AppCompatActivity {

    // Declaration of Button type Elements
    private Button m_PartnerUpButton = null;
    private Button m_FollowButton    = null;
    private Button m_MessageButton   = null;

    //Declaration of TextView type elements
    private TextView m_FollowerCount = null;
    private TextView m_PartnerCount  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //Initialization of Activity elements
        m_FollowButton = findViewById(R.id.follow_button);
        m_PartnerUpButton = findViewById(R.id.partner_up_button);
        m_MessageButton = findViewById(R.id.follow_button);
        m_FollowerCount = findViewById(R.id.follower_count);
        m_PartnerCount = findViewById(R.id.partner_count);


        //Follow Button's functionality to increase the follower count if clicked
        m_FollowButton.setOnClickListener((View v) -> {
            int folCount = Integer.parseInt(m_FollowerCount.getText().toString());

            folCount++;
            m_FollowerCount.setText(String.valueOf(folCount));
        });

        //Partner Up Button's functionality to increase the partner count if clicked
        m_PartnerUpButton.setOnClickListener((View v) -> {
            int partnerCount = Integer.parseInt((m_PartnerCount.getText().toString()));

            partnerCount++;
            m_PartnerCount.setText(String.valueOf(partnerCount));
        });
    }
}
