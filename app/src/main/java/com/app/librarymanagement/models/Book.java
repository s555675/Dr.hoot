package com.app.librarymanagement.models;

import androidx.annotation.NonNull;

public class Book {
    private String id;
    private String auth_id;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String rating;
    private String published;
    private int count;
    private int likes;

    public Book(){}
    public Book(String id, String auth_id,String name, String shortDescription, String longDescription,
                String rating, String published, int count, int likes) {
        this.id = id;
        this.auth_id = auth_id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.rating = rating;
        this.count = count;
        this.published = published;
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
