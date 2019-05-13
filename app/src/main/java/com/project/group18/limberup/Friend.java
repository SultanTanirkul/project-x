package com.project.group18.limberup;

import org.json.JSONException;
import org.json.JSONObject;

public class Friend {
    String id;
    String friend_id;
    String status;

    public Friend(JSONObject friendJson){
        try {
            id = friendJson.getString("_id");
            friend_id = friendJson.getString("friend_id");
            status = friendJson.getString("status");
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
}
