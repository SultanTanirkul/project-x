package com.project.group18.limberup;

import java.util.ArrayList;

public class Event {
    private String id = null;
    private String title     = null;
    private String host      = null;
    private EventCategoryEnum category  = null;
    private String location  = null;
    private int playerLimit  = -1;
    private int playerNumber = -1;
    private String date      = null;
    private ArrayList<User> players = null;


    public Event(String id, String title, String host, EventCategoryEnum category, String location, int playerLimit, int playerNumber, String date)
    {
        this.title = title;
        this.host = host;
        this.category = category;
        this.location = location;
        this.playerLimit = playerLimit;
        this.playerNumber = playerNumber;
        this.date = date;
        this.id = id;
        this.players = new ArrayList<>();
    }
    public String getId(){return this.id;}
    public String getTitle(){ return this.title;}
    public String getHoster(){ return this.host;}
    public EventCategoryEnum getCategory(){ return this.category;}
    public String getLocation(){ return this.location;}
    public int getPlayerLimit(){ return this.playerLimit;}
    public int getPlayerNumber(){ return this.playerNumber;}
    public String getDate(){return this.date;}

    public boolean addPlayer(User user){
        if(user == null){
            throw new NullPointerException("User is null object");
        }
        players.add(user);
        return true;
    }
}
