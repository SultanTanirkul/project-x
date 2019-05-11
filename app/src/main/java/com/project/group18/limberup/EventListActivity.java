package com.project.group18.limberup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    private ArrayList<Event> events =  new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        ServerOp serverOp = ServerOp.getInstance(getApplicationContext());
        serverOp.addToRequestQueue(serverOp.postRequest("https://limberup.herokuapp.com/api/activity/read", params, (s) ->{
            try {
                JSONArray response = new JSONArray(s);
                setActivities(response);
                setupActivityGrid();
            } catch(JSONException e){
                Log.i("---->", "setActivities: " + e.toString());
            }
        }));
        Event tempEvent = new Event("1","Louis's 5-Side", "Louis", EventCategoryEnum.FOOTBALL, "Surrey Sports Park, Richard Meyjes, Rd Guildford, GU2 7AD", 10, 16, "10:30", null);

        events.add(tempEvent);
        events.add(tempEvent);
        events.add(tempEvent);
        events.add(tempEvent);
        events.add(tempEvent);
        events.add(tempEvent);

        ArrayAdapter<Event> adapter =  new EventArrayAdapter(this, 0, events);

        ListView eventListView = findViewById(R.id.events_event_list);

        eventListView.setAdapter(adapter);

    }
}
