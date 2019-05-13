package com.project.group18.limberup;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.prof.rssparser.Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EventActivity extends AppCompatActivity {
    private ArrayList<User> users = new ArrayList<>();
    private TextView eventTitleView;
    private TextView locationTextView;
    private TextView dateTextView;
    private TextView descriptionTextView;
    private ListView participantListView;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        participantListView = findViewById(R.id.participantListView);
        eventTitleView = findViewById(R.id.eventTitleTextView);
        locationTextView = findViewById(R.id.locationTextView);
        dateTextView = findViewById(R.id.dateTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        participantListView = findViewById(R.id.explore_list_view);

        SharedPreferences sharedPref = EventActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String token = sharedPref.getString("token", null);
        HashMap params = new HashMap<>();
        params.put("token", token);
        String event_id = getIntent().getStringExtra("event_id");
        Log.i("IDDD", "onCreate: " + event_id);
        params.put("_id", event_id);
            ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
            serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/event/read", params, (s) -> {
                try {
                    setEvents(new JSONArray(s).getJSONObject(0));
                } catch(JSONException e){
                    e.printStackTrace();
                }
                }));
    }

    public void setEvents(JSONObject eventJson) {
        event = new Event(eventJson);
        eventTitleView.setText(event.getName());
        descriptionTextView.setText(event.description);
    }

    public void setUsers(JSONArray usersJson){
        for(int i = 0; i < usersJson.length(); i++){
            try {
                users.add(new User(usersJson.getJSONObject(i)));
            } catch(JSONException e){
                e.printStackTrace();
            }
        }

        ArrayAdapter<User> adapter =  new ExploreArrayAdapter(this, 0, users);

        participantListView.setAdapter(adapter);

    }

}
