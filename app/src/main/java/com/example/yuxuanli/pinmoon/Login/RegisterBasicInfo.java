package com.example.yuxuanli.pinmoon.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuxuanli.pinmoon.R;
import com.example.yuxuanli.pinmoon.Utils.FirebaseMethods;
import com.example.yuxuanli.pinmoon.Utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class RegisterBasicInfo extends AppCompatActivity{
    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private String email, username, password;
    private EditText mEmail, mPassword, mUsername;
    private TextView loadingPleaseWait;
    private Button btnRegister;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String append = "";

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerbasic_info);
        mContext = RegisterBasicInfo.this;
        Log.d(TAG, "onCreate: started");

        initWidgets();
        init();
    }

    private void init(){
        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();

                if (checkInputs(email, username, password))
                {
                    Intent intent = new Intent(RegisterBasicInfo.this, RegisterGender.class);
                    User user = new User("","","","",email, username, 0, false, false, false, false, "");
                    intent.putExtra("password", password);
                    intent.putExtra("classUser", user);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean checkInputs(String email, String username, String password) {
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if (email.equals("") || username.equals("") || password.equals(""))
        {
            Toast.makeText(mContext, "All fields must be filed out.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Below code checks if the email id is valid or not.
        if (!email.matches(emailPattern))
        {
            Toast.makeText(getApplicationContext(),"Invalid email address, enter valid email id and click on Continue",Toast.LENGTH_SHORT).show();
            return false;

        }


        return true;
    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: initializing widgets");
        mEmail = (EditText) findViewById(R.id.input_email);
        mUsername = (EditText) findViewById(R.id.input_username);
        btnRegister = (Button) findViewById(R.id.btn_register);
        mPassword = (EditText) findViewById(R.id.input_password);
        mContext = RegisterBasicInfo.this;

    }
}
