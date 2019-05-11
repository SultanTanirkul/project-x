package com.project.group18.limberup;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EventListActivity extends AppCompatActivity {

    private ArrayList<Event> events =  new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        SharedPreferences sharedPref = EventListActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", null);
        HashMap params = new HashMap<>();
        params.put("token", token);
        params.put("activity", getIntent().getStringExtra("id"));
        ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
        serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/event/read", params, (s) ->{
            try {
                Log.i("---->", "setActivities: " + s);
                JSONArray response = new JSONArray(s);
                setEvents(response);
            } catch(JSONException e){
                Log.i("---->", "setActivities: " + e.toString());
            }
        }));
    }

    public void setEvents(JSONArray response) throws JSONException{

        for(int i = 0; i < response.length(); i++){
            events.add(new Event(response.getJSONObject(i)));
        }
        ArrayAdapter<Event> adapter =  new EventArrayAdapter(this, 0, events);

        ListView eventListView = findViewById(R.id.events_event_list);

        eventListView.setAdapter(adapter);
    }
}
