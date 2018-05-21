package com.example.yuxuanli.pinmoon.Utils;

import java.util.List;

/**
 * Created by yuxuanli on 4/10/18.
 */

public class User {
    private String user_id;
    private String phone_number;
    private String email;
    private String username;
    private int profile_photo;
    private List<String> interests;
    private String description;
    private String sex;


    public User() {
    }

    public User(String sex, String user_id, String phone_number, String email, String username, int profile_photo, List<String> interests, String description) {
        this.sex = sex;
        this.user_id = user_id;
        this.phone_number = phone_number;
        this.email = email;
        this.username = username;
        this.profile_photo = profile_photo;
        this.interests = interests;
        this.description = description;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(int profile_photo) {
        this.profile_photo = profile_photo;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", phone_number=" + phone_number +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", profile_photo='" + profile_photo + '\'' +
                ", interests=" + interests +
                ", description='" + description + '\'' +
                '}';
    }
}
