package com.arianensis.mooglegaps.api_retrofit_flickr.Model;

import java.util.ArrayList;

public class Photos {
    // all the values of the JSON object for Retrofit to recognise
    public int page;
    public int pages;
    public int perpage;
    public int total;
    public ArrayList<Photo> photo;

    // getters of the needed values
    public ArrayList<Photo> getPhoto() { return photo; }

    // method to get the list of urls
    public ArrayList<String> generateListOfURL() {
        ArrayList<String> list = new ArrayList<>();
        for (Photo photo : getPhoto()) list.add(photo.generateURL());
        return list;
    }
}
