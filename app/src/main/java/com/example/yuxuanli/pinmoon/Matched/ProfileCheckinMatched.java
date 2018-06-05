package com.example.yuxuanli.pinmoon.Matched;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yuxuanli.pinmoon.R;
import com.example.yuxuanli.pinmoon.Utils.CalculateAge;
import com.example.yuxuanli.pinmoon.Utils.User;

public class ProfileCheckinMatched extends AppCompatActivity {
    private static final String TAG = "ProfileCheckinMatched";

    private User user;
    private Context mContext = ProfileCheckinMatched.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_checkin_matched);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("classUser");
        Log.d(TAG, "onCreate: user name is" + user.getUsername());

        TextView toolbar = (TextView) findViewById(R.id.toolbartag);
        toolbar.setText("Matched");

        TextView profile_name = (TextView) findViewById(R.id.profile_name);
        TextView profile_distance = (TextView) findViewById(R.id.profile_distance);
        TextView profile_numbers = (TextView) findViewById(R.id.profile_number);
        TextView profile_email = (TextView) findViewById(R.id.profile_email);
        ImageView imageView = (ImageView) findViewById(R.id.image_matched);
        TextView profile_bio = (TextView) findViewById(R.id.bio_match);
        TextView profile_interest = (TextView) findViewById(R.id.interests_match);

        CalculateAge cal = new CalculateAge(user.getDateOfBirth());
        int age = cal.getAge();

        profile_name.setText(user.getUsername() + ", " + age);
        profile_email.setText(user.getEmail());
        profile_numbers.setText(user.getPhone_number());
        profile_bio.setText(user.getDescription());
        //append interests
        StringBuilder interest = new StringBuilder();
        if (user.isSports()) {
            interest.append("Sports   ");
        }
        if (user.isFishing()) {
            interest.append("Fishing   ");
        }
        if (user.isMusic()) {
            interest.append("Music   ");
        }
        if (user.isTravel()) {
            interest.append("Travel   ");
        }

        profile_interest.setText(interest.toString());

        String profileImageUrl = user.getProfileImageUrl();
        switch (profileImageUrl) {
            case "defaultFemale":
                Glide.with(mContext).load(R.drawable.default_woman).into(imageView);
                break;
            case "defaultMale":
                Glide.with(mContext).load(R.drawable.default_man).into(imageView);
                break;
            default:
                Glide.with(mContext).load(profileImageUrl).into(imageView);
                break;
        }

        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
