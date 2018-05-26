package com.example.yuxuanli.pinmoon.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.yuxuanli.pinmoon.R;
import com.example.yuxuanli.pinmoon.Utils.User;

public class RegisterAge extends AppCompatActivity {

    String password;
    User user;

    private DatePicker ageSelectionPicker;
    private Button ageContinueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_age);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("classUser");
        password = intent.getStringExtra("password");

        ageSelectionPicker = (DatePicker) findViewById(R.id.ageSelectionPicker);

        ageContinueButton = (Button) findViewById(R.id.ageContinueButton);

        ageContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openHobbiesEntryPage();
            }
        });


    }

    public void openHobbiesEntryPage()
    {
        Intent intent = new Intent(this, RegisterHobby.class);
        intent.putExtra("password", password);
        intent.putExtra("classUser", user);
        startActivity(intent);
    }
}
