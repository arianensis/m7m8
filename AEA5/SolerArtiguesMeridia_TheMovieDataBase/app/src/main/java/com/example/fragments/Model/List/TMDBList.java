package com.example.fragments.Model.List;

public class TMDBList {
    public String name;
    public int item_count;
    public int id;

    public TMDBList(String title, int count) {
        this.name = title;
        this.item_count = count;
    }

    public String getName() {
        return name;
    }

    public int getItemCount() {
        return item_count;
    }

    public int getId() { return id; }
}
