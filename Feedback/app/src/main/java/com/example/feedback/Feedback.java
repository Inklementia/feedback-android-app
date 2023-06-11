package com.example.feedback;

// Class for keeping feedback properties
public class Feedback {
    public long Id;
    public String username;
    public String feedback;
    public String date;
    public String type;
    public float rating;

    public Feedback(long id, String username, String feedback, String date, String type, float rating) {
        this.Id = id;
        this.username = username;
        this.feedback = feedback;
        this.date = date;
        this.type = type;
        this.rating = rating;
    }

}


