package com.project.group18.limberup;

public class Feed {
    private String title = null;
    private String description = null;
    private String image = null;
    private String link = null;
    private String dateTime = null;


    public Feed(String title, String description, String image, String link, String dateTime){
        this.title = title;
        this.dateTime = dateTime;
        this.description = description;
        this.image = image;
        this.link = link;
    }

    public String getTitle(){return this.title;}
    public String getDescription(){return this.description;}
    public String getImage(){return this.image;}
    public String getLink(){return this.link;}
    public String getDateTime(){return this.dateTime;}
}
