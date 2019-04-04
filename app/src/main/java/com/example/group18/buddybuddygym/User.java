package com.example.group18.buddybuddygym;

public class User {
    private String username;
    private String password;
    private String interests;
    private String email;
    private String bio;
    private String image;


    public User(String username, String password, String email, String bio, String interests){
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.interests = interests;
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
}
