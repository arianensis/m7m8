package com.arianensis.mooglegaps.api_retrofit_flickr.Model;

public class Photo {
    // all the values of the JSON object for Retrofit to recognise
    public String id;
    public String owner;
    public String secret;
    public String server;
    public int farm;
    public String title;
    public int ispublic;
    public int isfriend;
    public int isfamily;

    // getters of the needed values
    public String getServer() { return server; }
    public String getId() { return id; }
    public String getSecret() { return secret; }

    // method to generate the url from a valid Photo object
    public String generateLocalURL() {
        return "/"+server+"/"+id+"_"+secret+".jpg";
    }
    public String generateURL() {
        return "https://live.staticflickr.com"+generateLocalURL();
    }
}
