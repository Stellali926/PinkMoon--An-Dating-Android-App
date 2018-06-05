package com.example.yuxuanli.pinmoon.Utils;

import java.util.Calendar;

/**
 * Created by yuxuanli on 6/4/18.
 */

public class CalculateAge {
    private int age;

    public CalculateAge(String dob) {
        String[] splitDOB = dob.split("-");
        setAge(Integer.parseInt(splitDOB[2]),Integer.parseInt(splitDOB[0]),Integer.parseInt(splitDOB[1]));
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int year, int month, int day)
    {
        Calendar dateOfBirth = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dateOfBirth.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dateOfBirth.get(Calendar.DAY_OF_YEAR))
        {
            age--;
        }

        this.age = age;
    }
}
