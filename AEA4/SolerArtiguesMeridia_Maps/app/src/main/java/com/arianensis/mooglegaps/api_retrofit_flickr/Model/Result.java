package com.arianensis.mooglegaps.api_retrofit_flickr.Model;

public class Result {
    // all the values of the JSON object for Retrofit to recognise
    public Photos photos;
    public String stat;

    // getters of the needed values
    public Photos getPhotos() { return photos; }
    public String getStat() { return stat; }
}
