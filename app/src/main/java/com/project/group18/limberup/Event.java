package com.project.group18.limberup;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Event {

    String id;
    String name;
    String description;
    ArrayList<String> participants = new ArrayList<>();
    ArrayList<String> participants_inquired = new ArrayList<>();

    public Event(JSONObject eventJson){
        try {
            this.id = eventJson.getString("_id");
            this.name = eventJson.getString("name");
            this.description = eventJson.getString("description");
            JSONArray participantsJson = eventJson.getJSONArray("participants");
            for(int i = 0; i < participantsJson.length(); i++){
                Log.i("PARTICIPANTS CALLED", "Event: ");
                participants.add(participantsJson.getString(i));
            }
            JSONArray participantsInquiredJson = eventJson.getJSONArray("participants_inquired");
            for(int i = 0; i < participantsInquiredJson.length(); i++){
                participants_inquired.add(participantsInquiredJson.getString(i));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }


}