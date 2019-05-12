package com.project.group18.limberup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendListActivity extends AppCompatActivity {
    ArrayList<User> accepted = new ArrayList<>();
    ArrayList<User> requested = new ArrayList<>();
    ArrayList<User> pending = new ArrayList<>();
    ListView acceptedList;
    ListView pendingList;
    ListView requestedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        acceptedList = (ListView) findViewById(R.id.acceptedlist);
        pendingList = (ListView) findViewById(R.id.pendingList);
        requestedList = (ListView) findViewById(R.id.requestedList);

        SharedPreferences sharedPref = FriendListActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        HashMap params = new HashMap<>();
        params.put("token", token);
        ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
        serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/user/friend/read", params, (s) ->{
            try {
                JSONObject response = new JSONObject(s);
                setFriends(response);
            } catch(JSONException e){
                Log.i("---->", "setActivities: " + e.toString());
            }
        }));
    }


    public void setFriends(JSONObject friendsJson){
        try{
            JSONArray pendingJson = new JSONArray();
            JSONArray acceptedJson = new JSONArray();
            JSONArray requestedJson = new JSONArray();
            try {
                pendingJson = friendsJson.getJSONArray("pending");
                Log.i("--->", "added to pending");
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                acceptedJson = friendsJson.getJSONArray("accepted");
                Log.i("--->", "added to accepted");
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                requestedJson = friendsJson.getJSONArray("requested");
                Log.i("--->", "added to requested");
            } catch(Exception e){
                e.printStackTrace();
            }

            populateList(requestedJson, requested);
            populateList(acceptedJson, accepted);
            populateList(pendingJson, pending);

            ArrayAdapter<User> acceptedAdapter =  new ExploreArrayAdapter(this, 0, accepted);
            ArrayAdapter<User> pendingAdapter =  new ExploreArrayAdapter(this, 0, pending);
            ArrayAdapter<User> requestedAdapter =  new ExploreArrayAdapter(this, 0, requested);

            Log.i("JSONS", "acceptedJson: " + pendingJson.toString());

            acceptedList.setAdapter(acceptedAdapter);
            pendingList.setAdapter(pendingAdapter);
            requestedList.setAdapter(requestedAdapter);

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public ArrayList<User> populateList(JSONArray listJson, ArrayList friendslist) throws JSONException{
        for(int i = 0; i < listJson.length(); i++){
            friendslist.add(new User(listJson.getJSONObject(i)));
        }
        return friendslist;
    }
}
