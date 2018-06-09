package com.example.yuxuanli.pinmoon.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.yuxuanli.pinmoon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by yuxuanli on 4/10/18.
 */

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    private Context mContext;

    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mContext = context;

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public boolean checkIfUsernameExists(String username, DataSnapshot datasnapshot) {
        Log.d(TAG, "checkIfUsernameExists: checking if " + username + " already exists.");

        User user = new User();

        for (DataSnapshot ds : datasnapshot.child(userID).getChildren()) { //datasnapshot contains every node in the setting
            Log.d(TAG, "checkIfUsernameExists: datasnapshot: " + ds);

            user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG, "checkIfUsernameExists: username: " + user.getUsername());

            if (StringManipulation.expandUsername(user.getUsername()).equals(username)) {
                Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + user.getUsername());
                return true;

            }
        }

        return false;
    }

    /**
     * RegisterBasicInfo a new email and password to Firebase Authentication
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail: " + task.isSuccessful());
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //send verification email
                            sendVerificationEmail();
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                Toast.makeText(mContext, "couldn't send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    public void addNewUser(User user) {

        if (user.getSex().equals("female")) {
            myRef.child(mContext.getString(R.string.dbfemale))
                    .child(userID)
                    .setValue(user);

        } else {
            myRef.child(mContext.getString(R.string.dbmale))
                    .child(userID)
                    .setValue(user);

        }

    }


    public User getUser (DataSnapshot dataSnapshot, String sex, String uid) {
        User user = new User();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            if (ds.getKey().equals(sex)) {

                User temp = ds.child(uid).getValue(User.class);

                user.setUsername(temp.getUsername());

                user.setProfileImageUrl(temp.getProfileImageUrl());

                user.setDateOfBirth(temp.getDateOfBirth());

                user.setDescription(temp.getDescription());

                user.setSports(temp.isSports());

                user.setFishing(temp.isFishing());

                user.setTravel(temp.isTravel());

                user.setMusic(temp.isMusic());

                user.setEmail(temp.getEmail());

                user.setPhone_number(temp.getPhone_number());

                user.setLatitude(temp.getLatitude());

                user.setLongtitude(temp.getLongtitude());
            }
        }

        return user;
    }
}
