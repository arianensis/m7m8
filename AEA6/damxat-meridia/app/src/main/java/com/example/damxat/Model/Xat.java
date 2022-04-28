package com.example.damxat.Model;
/*
 * Model of the chat item. Each instance contains the message and references to the
 * user who sent it and whom it was sent to
 * The class mirrors the structure of the sub-object on the Firebase database
 */
public class Xat {

    String sender;
    String receiver;
    String message;

    public Xat(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }


    public Xat() {

    }


    public Xat(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
