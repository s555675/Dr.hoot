package com.app.librarymanagement.models;

import androidx.annotation.NonNull;

public class Author {
    private String id;
    private String name;
    private String rating;
    private int age;
    private String gender;
    public Author(){}
    public Author(String id, String name, String rating, int age, String gender) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.age = age;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
