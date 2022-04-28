package com.example.damxat.Model;
/*
 * Model of the user item. It contains the username and id, as well as a string indicating
 * the status (online or offline)
 * The class mirrors the structure of the sub-object on the Firebase database
 */
public class User {
    String id;
    String username;
    String status;

    public User(String id, String username, String status) {
        this.id = id;
        this.username = username;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
