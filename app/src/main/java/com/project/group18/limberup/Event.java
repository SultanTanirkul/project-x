package com.project.group18.limberup;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Event {
    private String id                   = null;
    private String title                = null;
    private String host                 = null;
    private EventCategoryEnum category  = null;
    private String location             = null;
    private int playerLimit             = -1;
    private int playerNumberMin            = -1;
    private String date                 = null;
    private ArrayList<User> players     = null;
    private LatLng latLng               = null;
    private String time                 = null;


    public Event(String id, String title, String host, EventCategoryEnum category, String location, int playerLimit, int playerNumberMin, String date, String time, LatLng latLng)
    {
        this.title = title;
        this.host = host;
        this.category = category;
        this.location = location;
        this.playerLimit = playerLimit;
        this.playerNumberMin = playerNumberMin;
        this.date = date;
        this.id = id;
        this.time = time;
        this.players = new ArrayList<>();
        this.latLng = latLng;
    }
    public String getId(){return this.id;}
    public String getTitle(){ return this.title;}
    public String getHoster(){ return this.host;}
    public EventCategoryEnum getCategory(){ return this.category;}
    public String getLocation(){ return this.location;}
    public int getPlayerLimit(){ return this.playerLimit;}
    public int getPlayerNumberMin(){ return this.playerNumberMin;}
    public String getDate(){return this.date;}
    public LatLng getLat(){return this.latLng;}
    public String getTime(){return this.time;}

    public boolean addPlayer(User user){
        if(user == null){
            throw new NullPointerException("User is null object");
        }
        players.add(user);
        return true;
    }
}
