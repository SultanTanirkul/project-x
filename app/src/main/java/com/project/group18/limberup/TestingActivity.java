package com.project.group18.limberup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class TestingActivity extends AppCompatActivity {

    private Button m_LogOut_Button = null;
    private Button m_Event_Invitations_Button = null;
    private Button m_Event_Recommendations_Button = null;
    private Button m_Dashboard_Explore_Button = null;
    private Button m_Dashboard_Feed_Button = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);



        // Initialization of View Elements.
        m_LogOut_Button = findViewById(R.id.sign_out_button);
        m_Event_Invitations_Button = findViewById(R.id.event_invitations);
        m_Dashboard_Feed_Button = findViewById(R.id.dashboard_feed_button);

        SharedPreferences sharedPref = TestingActivity.this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        double myLatitude = 0.0;
        double myLongitude = 0.0;
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location myLocation = locationManager.getLastKnownLocation(provider);
            myLatitude = myLocation.getLatitude();
            myLongitude = myLocation.getLongitude();
            Log.i("---->", "Latt: " + myLatitude);
            Log.i("---->", "Long: " + myLongitude);
        }

        String token = sharedPref.getString("token", null);
        ServerOp checkTokenReq = ServerOp.getInstance(getApplicationContext());
        Map locationUpdate = new HashMap<>();
        locationUpdate.put("token", token);
        JSONArray coordinates = new JSONArray();
        try {
            coordinates.put(myLatitude);
            coordinates.put(myLongitude);
            locationUpdate.put("coordinates", coordinates.toString());
            checkTokenReq.addToRequestQueue(checkTokenReq.postRequest("https://limberup.herokuapp.com/api/user/update", locationUpdate, (s) -> {  }));

        }catch (JSONException e){
            Log.i("---->", "Error: " + e);
        }

        // Some functionality for Log out button.
        m_LogOut_Button.setOnClickListener((View v) -> {
            sharedPref.edit().clear().commit();
            Intent intent = new Intent(TestingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        m_Event_Invitations_Button.setOnClickListener((View v) -> {
            Intent intent = new Intent(TestingActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        m_Event_Recommendations_Button.setOnClickListener((View v) -> {
            Intent intent = new Intent(TestingActivity.this, ActivityCategory.class);
            startActivity(intent);
        });

        m_Dashboard_Explore_Button.setOnClickListener((View v) -> {
            Intent intent = new Intent(TestingActivity.this, ExploreActivity.class);
            startActivity(intent);
        });

        m_Dashboard_Feed_Button.setOnClickListener((View v) -> {
            Intent intent = new Intent(TestingActivity.this, FeedActivity.class);
            startActivity(intent);
        });
    }


}
