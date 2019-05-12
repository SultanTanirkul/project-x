package com.project.group18.limberup;

import org.json.JSONException;
import org.json.JSONObject;

public class Friend {
    String id;
    String friend_id;
    String status;

    public Friend(JSONObject friendJson) throws JSONException {
        id = friendJson.getString("id");
        friend_id = friendJson.getString("friend_id");
        status = friendJson.getString("status");
    }
}
