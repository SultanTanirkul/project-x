package com.project.group18.limberup;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        sharedPref = DashboardActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);

        Log.i("---->", "Latt: " + sharedPref.getString("offline", ""));
        if(Integer.parseInt(sharedPref.getString("offline", "false")) == 1){
            HashMap params = new HashMap<>();
            params.put("token", token);
            params.put("username", sharedPref.getString("name", ""));
            Log.i("---->", "Latt: " + sharedPref.getString("name", ""));
            params.put("bio", sharedPref.getString("bio", ""));
            Log.i("---->", "Latt: " + sharedPref.getString("bio", ""));

            ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
            serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/user/update", params, (s) -> { }));
        }

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

        ServerOp checkTokenReq = ServerOp.getInstance(getApplicationContext());
        Map locationUpdate = new HashMap<>();
        locationUpdate.put("token", token);
        JSONArray coordinates = new JSONArray();
        try {
            coordinates.put(myLatitude);
            coordinates.put(myLongitude);
            locationUpdate.put("coordinates", coordinates.toString());
            checkTokenReq.addToRequestQueue(checkTokenReq.postRequest("https://limberup.herokuapp.com/api/user/update", locationUpdate, (s) -> {
            }));

        } catch (
                JSONException e) {
            Log.i("---->", "Error: " + e);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.dashboard_feed_button) {
            Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_activities) {
            Intent intent = new Intent(DashboardActivity.this, ActivityCategory.class);
            startActivity(intent);

        } else if (id == R.id.nav_explore) {
            Intent intent = new Intent(DashboardActivity.this, ExploreActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_feed) {
            Intent intent = new Intent(DashboardActivity.this, FeedActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            // TODO finish setting
        } else if (id == R.id.sign_out_button) {
            sharedPref.edit().clear().commit();
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.nav_profile)
        {
            Intent intent = new Intent(DashboardActivity.this, UserProfileActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
