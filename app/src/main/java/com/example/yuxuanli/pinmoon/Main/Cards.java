package com.example.yuxuanli.pinmoon.Main;

/**
 * Created by yuxuanli on 5/28/18.
 */

public class Cards {
    private String userId;
    private String name;
    public Cards(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
