package com.project.group18.limberup;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class ExploreActivity extends AppCompatActivity {

    private ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        SharedPreferences sharedPref = ExploreActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        HashMap params = new HashMap<>();
        params.put("token", token);
        ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
        serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/user/find", params, (s) ->{
            try {
                JSONArray response = new JSONArray(s);
                setUsers(response);
            } catch(JSONException e){
                Log.i("---->", "setActivities: " + e.toString());
            }
        }));

        //exploreListView.setOnItemClickListener(new);

    }

    public void setUsers(JSONArray userJson){
        Log.i("---->", "setUsers: " + userJson.length());
        for(int i = 0; i < userJson.length(); i++){
            try {
                users.add(new User(userJson.getJSONObject(i)));
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
        ArrayAdapter<User> adapter =  new ExploreArrayAdapter(this, 0, users);


        ListView exploreListView = findViewById(R.id.explore_list_view);

        exploreListView.setAdapter(adapter);
    }


}
