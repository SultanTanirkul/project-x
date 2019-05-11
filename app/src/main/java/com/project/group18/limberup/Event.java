package com.project.group18.limberup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Event {
    private String id = null;
    private String title     = null;
    private String host      = null;
    private String activity = null;
    private int[] location  = new int[2];
    private int playerLimit  = -1;
    private int playerNumberMin = -1;
    private String date      = null;
    private String description = null;
    private ArrayList<String> participants = null;


    public Event(String id, String title, String host, String activity, int[] location, int playerLimit, int playerNumber, String date)
    {
        this.title = title;
        this.host = host;
        this.activity = activity;
        this.location = location;
        this.playerLimit = playerLimit;
        this.date = date;
        this.id = id;
        this.participants = new ArrayList<>();
    }

    public Event(JSONObject eventJson){
        try {
            this.title = eventJson.getString("name");
            this.host = eventJson.getString("host");
            this.activity = eventJson.getString("activity");
            this.location[0] = eventJson.getJSONArray("coordinates").getInt(0);
            this.location[0] = eventJson.getJSONArray("coordinates").getInt(1);
            this.date = eventJson.getString("time");
            this.description = eventJson.getString("description");
            JSONArray participantsJson = eventJson.getJSONArray("participants");
            this.participants = new ArrayList<>();
            for (int i = 0; i < participantsJson.length(); i++) {
                this.participants.add(participantsJson.getString(i));
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    public String getId(){return this.id;}
    public String getTitle(){ return this.title;}
    public String getHoster(){ return this.host;}
    public String getActivity(){ return this.activity;}
    public int[] getLocation(){ return this.location;}
    public int getPlayerLimit(){ return this.playerLimit;}
    public String getDate(){return this.date;}
}
