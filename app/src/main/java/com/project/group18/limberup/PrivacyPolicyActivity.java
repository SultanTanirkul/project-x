package com.project.group18.limberup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private TextView mPrivacyTitle = null;
    private TextView mPrivacyText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy_form);

        mPrivacyTitle = findViewById(R.id.textView13);
        mPrivacyText = findViewById(R.id.textView14);
    }
}
