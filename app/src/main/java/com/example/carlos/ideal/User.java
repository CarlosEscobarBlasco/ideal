package com.example.carlos.ideal;

import android.media.Image;

/**
 * Created by carlos on 15/11/2017.
 */

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String description;
    private Image photo;

    public User(int id, String name, String password, String email, String description) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.description = description;
       // this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }
}
