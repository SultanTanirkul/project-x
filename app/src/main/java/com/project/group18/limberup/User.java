package com.project.group18.limberup;

public class User {
    private String username  = null;
    private String password  = null;
    private String interests = null;
    private String email     = null;
    private String bio       = null;
    private String image     = null;
    private int age          = -1;


    public User(String username, String password, String email, String bio, String interests, String image, int age){
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.interests = interests;
        this.image = image;
        this.age = age;
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

    public String getInterests(){
        return interests;
    }
    public String getImage(){return image;}

    public int getAge(){return age;}
}
