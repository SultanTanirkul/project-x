package com.project.group18.limberup;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class User {
    private String id = null;
    private String username  = null;
    private String password  = null;
    private ArrayList<String> interests = null;
    private String email     = null;
    private String bio       = null;
    private String image     = null;
    private int age          = -1;


    public User(String username, String password, String email, String bio, ArrayList<String> interests, String image, int age){
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.interests = interests;
        this.image = image;
        this.age = age;
    }

    public User(JSONObject userJson) throws JSONException {
        this.id = userJson.getString("_id");
        this.username = userJson.getString("username");
        this.email = userJson.getString("email");
        this.bio = userJson.getString("bio");
        this.interests = new ArrayList<>();
        JSONArray interestsJson = userJson.getJSONArray("interests");
        for(int i = 0; i < interests.size(); i++){
            interests.add(interestsJson.getJSONObject(i).getString("id"));
        }
        try {
            this.image = userJson.getString("profileImgUrl");
        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    public HashMap toJson(){
            HashMap userJson = new HashMap();
            userJson.put("id", this.id);
            userJson.put("username", this.getUsername());
            userJson.put("email", this.getEmail());
            userJson.put("bio", this.getBio());
            //userJson.put("interests", this.interests);
            userJson.put("profileImgUrl", this.getImage());
            return userJson;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getBio(){
        return bio;
    }

    public ArrayList<String> getInterests(){
        return interests;
    }
    public String getImage(){return image;}

    public int getAge(){return age;}

    public void setImageView(CircleImageView profilepic){
        Picasso.get().load(this.getImage()).into(profilepic);
    }

    public void setImage(String imageUrl){
        this.image = imageUrl;
    }
}
