package com.example.yuxuanli.pinmoon.Utils;

/**
 * Created by yuxuanli on 4/19/18.
 */

public class UserSettings {

    private User user;

    public UserSettings(User user) {
        this.user = user;
    }

    public UserSettings() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "user=" + user +
                '}';
    }
}
