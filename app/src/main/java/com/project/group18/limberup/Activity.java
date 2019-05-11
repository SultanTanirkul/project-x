package com.project.group18.limberup;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity {
    public String id;
    public String name;

    public Activity(JSONObject activityJson){
        try {
            id = activityJson.getString("_id");
            name = activityJson.getString("name");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
