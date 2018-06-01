package com.example.yuxuanli.pinmoon.Matched;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuxuanli.pinmoon.R;

public class ProfileCheckinMatched extends AppCompatActivity {

    private String name;
    private String email;
    private String numbers;
    private int photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_checkin_matched);

        Intent intent = getIntent();
        this.name = intent.getStringExtra("name");
        this.email = intent.getStringExtra("email");
        //this.photo = Integer.parseInt(intent.getStringExtra("photo"));
        this.numbers = intent.getStringExtra("number");

        TextView toolbar = (TextView) findViewById(R.id.toolbartag);
        toolbar.setText("Matched");

        TextView profile_name = (TextView) findViewById(R.id.profile_name);
        TextView profile_distance = (TextView) findViewById(R.id.profile_distance);
        TextView profile_numbers = (TextView) findViewById(R.id.profile_number);
        TextView profile_email = (TextView) findViewById(R.id.profile_email);
        ImageView imageView = (ImageView) findViewById(R.id.image_matched);

        profile_name.setText(name);
        profile_email.setText(email);
        profile_numbers.setText(numbers);
        imageView.setImageResource(photo);

        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
