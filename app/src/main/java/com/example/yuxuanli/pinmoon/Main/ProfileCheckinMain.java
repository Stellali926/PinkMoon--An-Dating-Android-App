package com.example.yuxuanli.pinmoon.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yuxuanli.pinmoon.R;

public class ProfileCheckinMain extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_checkin_main);

        mContext = ProfileCheckinMain.this;

        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView profileName = findViewById(R.id.name_main);
        ImageView profileImage = findViewById(R.id.profileImage);
        TextView profileBio = findViewById(R.id.bio_beforematch);
        TextView profileInterest = findViewById(R.id.interests_beforematch);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String bio = intent.getStringExtra("bio");
        String interest = intent.getStringExtra("interest");
        profileName.setText(name);
        profileBio.setText(bio);
        profileInterest.setText(interest);

        String profileImageUrl = intent.getStringExtra("photo");
        switch (profileImageUrl) {
            case "defaultFemale":
                Glide.with(mContext).load(R.drawable.default_woman).into(profileImage);
                break;
            case "defaultMale":
                Glide.with(mContext).load(R.drawable.default_man).into(profileImage);
                break;
            default:
                Glide.with(mContext).load(profileImageUrl).into(profileImage);
                break;
        }
    }
}
