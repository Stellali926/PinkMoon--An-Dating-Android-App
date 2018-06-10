package com.example.yuxuanli.pinmoon.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yuxuanli.pinmoon.Introduction.IntroductionMain;
import com.example.yuxuanli.pinmoon.R;
import com.example.yuxuanli.pinmoon.Utils.CalculateAge;
import com.example.yuxuanli.pinmoon.Utils.GPS;
import com.example.yuxuanli.pinmoon.Utils.TopNavigationViewHelper;
import com.example.yuxuanli.pinmoon.Utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 1;
    final private int MY_PERMISSIONS_REQUEST_LOCATION = 123;

    private Context mContext = MainActivity.this;
    private String userSex, lookforSex;
    private double latitude = 37.349642;
    private double longtitude = -121.938987;
    private String currentUID;
    private boolean sports, fish, music, travel;
    private String name, bio, interest;

    private NotificationHelper mNotificationHelper;
    private Cards cards_data[];
    private PhotoAdapter arrayAdapter;

    ListView listView;
    List<Cards> rowItems;

    GPS gps;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference usersDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDb = FirebaseDatabase.getInstance().getReference();
        mNotificationHelper = new NotificationHelper(this);

        setupFirebaseAuth();
        setupTopNavigationView();

        checkUserSex();

        rowItems  = new ArrayList<Cards>();
        arrayAdapter = new PhotoAdapter(this, R.layout.item, rowItems);

        updateSwipeCard();
    }

    private void updateLocation(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            gps = new GPS(this);
            Location location = gps.getLocation();
            // made the changes to set location only if location object is not null, else default location is taken
            if (location!=null)
            {
                this.latitude = location.getLatitude();
                this.longtitude = location.getLongitude();
            }

            DatabaseReference curDB = FirebaseDatabase.getInstance().getReference().child(userSex).child(currentUID);
            Map userLoc = new HashMap<>();
            userLoc.put("latitude", latitude);
            userLoc.put("longtitude", longtitude);
            curDB.updateChildren(userLoc);
        }
    }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_LOCATION: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                            updateLocation();
                        } else {
                            Toast.makeText(MainActivity.this, "Location Permission Denied. You have to give permission inorder to know the user range ", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }

                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

    private void updateSwipeCard() {
        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(lookforSex).child(userId).child("connections").child("dislikeme").child(currentUID).setValue(true);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(lookforSex).child(userId).child("connections").child("likeme").child(currentUID).setValue(true);

                //check matches
                isConnectionMatch(userId);

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });
    }

    private void isConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(userSex).child(currentUID).child("connections").child("likeme").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    //prompt user that match
                    //later change to notification
                    sendNotification();

                    usersDb.child(lookforSex).child(dataSnapshot.getKey()).child("connections").child("match_result").child(currentUID).setValue(true);
                    usersDb.child(userSex).child(currentUID).child("connections").child("match_result").child(dataSnapshot.getKey()).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void sendNotification() {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(mContext.getString(R.string.app_name), mContext.getString(R.string.match_notification));

        mNotificationHelper.getManager().notify(1, nb.build());
    }

    /**
     * check the user sex
     */
    public void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.currentUID = user.getUid();

            DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("male");
            maleDb.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    if (dataSnapshot.getKey().equals(currentUID)) {
                        Log.d(TAG, "onChildAdded: the sex is " + userSex);
                        userSex = "male";
                        //updateLocation
                        updateLocation();

                        lookforSex = dataSnapshot.getValue(User.class).getPreferSex();
                        findInterest(dataSnapshot);
                        getPotentialMatch();
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

                    if (dataSnapshot.getKey().equals(currentUID)) {
                        Log.d(TAG, "onChildAdded: the sex is " + userSex);
                        userSex = "female";
                        //updateLocation
                        updateLocation();

                        lookforSex = dataSnapshot.getValue(User.class).getPreferSex();
                        findInterest(dataSnapshot);
                        getPotentialMatch();
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
    }

    private void findInterest(DataSnapshot dataSnapshot) {
        sports = dataSnapshot.getValue(User.class).isSports();
        fish = dataSnapshot.getValue(User.class).isFishing();
        travel = dataSnapshot.getValue(User.class).isTravel();
        music = dataSnapshot.getValue(User.class).isMusic();
    }

    /**
     * show the lookforsex profile photos
     */
    public void getPotentialMatch() {
        DatabaseReference potentialMatch = FirebaseDatabase.getInstance().getReference().child(lookforSex);
        potentialMatch.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && !dataSnapshot.child("connections").child("dislikeme").hasChild(currentUID) && !dataSnapshot.child("connections").child("likeme").hasChild(currentUID) && !dataSnapshot.getKey().equals(currentUID)) {
                    User curUser = dataSnapshot.getValue(User.class);
                    boolean tempTravel = curUser.isTravel();
                    boolean tempFish = curUser.isFishing();
                    boolean tempMusic = curUser.isMusic();
                    boolean tempSports = curUser.isSports();

                    if (tempFish == fish || tempMusic == music || tempTravel == travel || tempSports == sports) {
                        //calculate age
                        String dob = curUser.getDateOfBirth();
                        CalculateAge cal = new CalculateAge(dob);
                        int age = cal.getAge();

                        //initialize card view
                        //check profile image first
                        String profileImageUrl = lookforSex.equals("female") ? "defaultFemale" : "defaultMale";
                        if (dataSnapshot.child("profileImageUrl").getValue() != null){
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }

                        String username = curUser.getUsername();
                        String bio = curUser.getDescription();
                        StringBuilder interest = new StringBuilder();
                        if (tempSports) {
                            interest.append("Sports   ");
                        }
                        if (tempFish) {
                            interest.append("Fishing   ");
                        }
                        if (tempMusic) {
                            interest.append("Music   ");
                        }
                        if (tempTravel) {
                            interest.append("Travel   ");
                        }

                        //calculate distance
                        gps = new GPS(mContext);
                        Log.d(TAG, "onChildAdded: the x, y of user is " + latitude + ", " + longtitude);
                        Log.d(TAG, "onChildAdded: the other user x y is " + curUser.getLatitude() + ", " + curUser.getLongtitude());
                        int distance = gps.calculateDistance(latitude, longtitude, curUser.getLatitude(), curUser.getLongtitude());

                        Cards item = new Cards(dataSnapshot.getKey(), username, age, profileImageUrl, bio, interest.toString(), distance);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
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

    public void DislikeBtn(View v) {
        if (rowItems.size() != 0) {
            Cards card_item = rowItems.get(0);

            String userId = card_item.getUserId();
            usersDb.child(lookforSex).child(userId).child("connections").child("dislikeme").child(currentUID).setValue(true);

            rowItems.remove(0);
            arrayAdapter.notifyDataSetChanged();

            Intent btnClick = new Intent(mContext, BtnDislikeActivity.class);
            btnClick.putExtra("url", card_item.getProfileImageUrl());
            startActivity(btnClick);
        }
    }

    public void LikeBtn(View v) {
        if (rowItems.size() != 0) {
            Cards card_item = rowItems.get(0);

            String userId = card_item.getUserId();
            usersDb.child(lookforSex).child(userId).child("connections").child("likeme").child(currentUID).setValue(true);

            //check matches
            isConnectionMatch(userId);

            rowItems.remove(0);
            arrayAdapter.notifyDataSetChanged();

            Intent btnClick = new Intent(mContext, BtnLikeActivity.class);
            btnClick.putExtra("url", card_item.getProfileImageUrl());
            startActivity(btnClick);
        }
    }


    /**
     * setup top tool bar
     */
    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = (BottomNavigationViewEx) findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    /**
     * check to see if the @param 'user' is logged in
     * @param user
     */
    private void checkCurrentUser(FirebaseUser user) {
        Log.d(TAG, "checkCurrentUser: checking if user is logged in");

        if (user == null) {
            Intent intent = new Intent(mContext, IntroductionMain.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: check user");
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
                checkCurrentUser(user);

                if (user != null) {
                    // user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in:" + user.getUid());
                } else {
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
