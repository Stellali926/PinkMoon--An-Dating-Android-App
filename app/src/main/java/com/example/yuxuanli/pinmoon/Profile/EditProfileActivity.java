package com.example.yuxuanli.pinmoon.Profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yuxuanli.pinmoon.Login.Login;
import com.example.yuxuanli.pinmoon.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mPhotoDB;

    private ImageView mProfileImage;
    private String userId, profileImageUri;
    private Uri resultUri;
    private String userSex;
    private EditText phoneNumber,aboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        TextView toolbar = (TextView) findViewById(R.id.toolbartag);
        toolbar.setText("Profile");

        setupFirebaseAuth();

        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mProfileImage = (ImageView)findViewById(R.id.profileImage);

        phoneNumber = (EditText)findViewById(R.id.edit_phone);

        aboutMe = (EditText)findViewById(R.id.edit_aboutme);

        userId = mAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "onCreate: user id is" + userId);

        checkUserSex();
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
    }

    private void getUserData(){
        mPhotoDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0)
                {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("profileImageUrl") != null)
                    {
                        profileImageUri = map.get("profileImageUrl").toString();
                        Log.d(TAG, "onDataChange: the profileImageUri is" + profileImageUri);

                        switch (profileImageUri) {
                            case "defaultFemale":
                                Glide.with(getApplication()).load(R.drawable.default_woman).into(mProfileImage);
                                break;
                            case "defaultMale":
                                Glide.with(getApplication()).load(R.drawable.default_man).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUri).into(mProfileImage);
                                break;
                        }
                    }
                    if(map.get("phone_number")!=null)
                    {
                        phoneNumber.setText(map.get("phone_number").toString());

                    }
                    if(map.get("description")!=null)
                    {
                        aboutMe.setText(map.get("description").toString());

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void saveUserPhoto() {
        if (resultUri != null) {
            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Map userInfo = new HashMap<>();
                    userInfo.put("profileImageUrl", downloadUrl.toString());
                    mPhotoDB.updateChildren(userInfo);

                    return;
                }
            });
        }
    }

    private void saveUserData()
    {
        Map userInfo = new HashMap<>();
        userInfo.put("phone_number", phoneNumber.getText().toString());
        userInfo.put("description", aboutMe.getText().toString());
        mPhotoDB.updateChildren(userInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);

        }
    }


    public void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("male");
        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getKey().equals(userId)) {
                    Log.d(TAG, "onChildAdded: the sex is male" );
                    userSex = "male";
                    mPhotoDB = FirebaseDatabase.getInstance().getReference().child(userSex).child(userId);
                    getUserData();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference femaleDb = FirebaseDatabase.getInstance().getReference().child("female");
        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getKey().equals(user.getUid())) {
                    Log.d(TAG, "onChildAdded: the sex is female" );
                    userSex = "female";
                    mPhotoDB = FirebaseDatabase.getInstance().getReference().child(userSex).child(userId);
                    getUserData();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void saveAndBack(View view) {
        saveUserPhoto();
        saveUserData();
        finish();
    }

    //----------------------------------------Firebase----------------------------------------

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in:" + user.getUid());
                } else {
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");

                    Log.d(TAG, "onAuthStateChanged: navigating back to login screen.");
                    Intent intent = new Intent(EditProfileActivity.this, Login.class);

                    //clear the activity stack， in case when sign out, the back button will bring the user back to the previous activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
