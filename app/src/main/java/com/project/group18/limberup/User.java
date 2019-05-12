package com.project.group18.limberup;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class User {
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
        this.username = userJson.getString("name");
        this.email = userJson.getString("email");
        this.bio = userJson.getString("bio");
        this.interests = new ArrayList<>();
        JSONArray interestsJson = userJson.getJSONArray("interests");
        for(int i = 0; i < interests.size(); i++){
            interests.add(interestsJson.getJSONObject(i).getString("id"));
        }
        this.image = userJson.getString("profileImgUrl");
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

    public void setImage(CircleImageView profilepic){
        Picasso.get().load(this.getImage()).into(profilepic);
    }


}
