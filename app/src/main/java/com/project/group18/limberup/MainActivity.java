package com.project.group18.limberup;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.project.group18.limberup.MapActivity.MY_PERMISSIONS_REQUEST_LOCATION;

public class MainActivity extends AppCompatActivity {

    // Declaration of View
    private TextView m_RegistrationLink = null;
    private TextView m_ForgetPassLink = null;
    private EditText m_Username_EditText = null;
    private EditText m_Password_EditText = null;
    private Button m_Login_Button = null;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationPermission();
        // Initialization of View Elements.
        m_Login_Button = findViewById(R.id.login_button);
        m_Username_EditText = findViewById(R.id.username_params);
        m_Password_EditText = findViewById(R.id.password_params);
        m_RegistrationLink = findViewById(R.id.link_register);
        m_ForgetPassLink = findViewById(R.id.link_forget_pass);

        dialog = ProgressDialog.show(MainActivity.this, "",
                "Loading. Please wait...", true);
        dialog.hide();

        Context context = this;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {


            // Handle previously logged in user
            if (checkIfLogged()) {
                Log.i("---->", "Checking if stored user exists!");
                loggedIn();
            }
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
            SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            String token = sharedPref.getString("token", null);
            if (token != null) {
                Intent intent = new Intent(MainActivity.this, UserProfileOfflineEditActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                m_Username_EditText.setInputType(0);
                m_Username_EditText.setText("Please connect to the internet");
                m_Password_EditText.setInputType(0);
                m_Password_EditText.setText("Please connect to the internet");
            }
        }

        // Some functionality for Login Button
        m_Login_Button.setOnClickListener((View v) -> {
            SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

            Log.i("---->", "Login button pressed");

            Map<String, String> userLoginParams = new HashMap<>();
            userLoginParams.put("username", m_Username_EditText.getText().toString());
            userLoginParams.put("password", m_Password_EditText.getText().toString());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", m_Username_EditText.getText().toString());
            editor.putString("password", (m_Password_EditText.getText().toString()));
            editor.apply();

            ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
            serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/authenticate", userLoginParams, (s) -> {
                Log.i("---->", "Response: " + s);
                dialog.hide();
                login(s);
            }));

        });

        // Some functionality for registration link/text.
        m_RegistrationLink.setOnClickListener((View v) -> {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

    }

    /**
     * Function for validation if user has been logged in to the system.
     *
     * @return boolean meaning success of the outcome.
     */
    private boolean checkIfLogged() {
        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);
        String password = sharedPref.getString("password", null);
        String token = sharedPref.getString("token", null);
        if (token != null) {
            Log.i("---->", "Token exists");
            ServerOp checkTokenReq = ServerOp.getInstance(getApplicationContext());
            Map authPrams = new HashMap<>();
            authPrams.put("token", token);
            checkTokenReq.addToRequestQueue(checkTokenReq.postRequest("https://limberup.herokuapp.com/api/verify", authPrams, (s) -> {
                if (Integer.parseInt(s) == 200) {
                    Log.i("---->", "Token authenticated: " + s);
                    loggedIn();
                } else {
                    Log.i("---->", "Token not authenticated: " + s);
                }
            }));
            Log.i("---->", "Checking stored login");
            if (password != null && username != null) {
                Log.i("---->", "Checking stored login");
                Map<String, String> userLoginParams = new HashMap<>();
                userLoginParams.put("username", username);
                userLoginParams.put("password", password);
                Log.i("---->", "User: " + username + " Pass: " + password);
                ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
                serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/authenticate", userLoginParams, (s) -> login(s)));
            }
        }
        Log.i("---->", "User not auto logged in");
        return false;

    }

    private void checkToken(String s) {
        Log.i("---->", s);
        if (Integer.parseInt(s) == 200) {
            Log.i("---->", "Token authenticated: " + s);
            loggedIn();
        } else {
            Log.i("---->", "Token not authenticated: " + s);
        }

    }

    /**
     * Login method
     */
    private boolean login(String s) {
        try {
            JSONObject response = new JSONObject(s);
            if (response.getBoolean("success")) {
                Toast.makeText(this, "Successfully authorized to the system, enjoy the app.", Toast.LENGTH_LONG).show();
                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("token", response.getString("token"));
                editor.apply();
                loggedIn();

            }else{
                Toast.makeText(this, "Username or Password is incorrect, please try again.", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Move to the dashboard layout
     */
    private void loggedIn() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}
