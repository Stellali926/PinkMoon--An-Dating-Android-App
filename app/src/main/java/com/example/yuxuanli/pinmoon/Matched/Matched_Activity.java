package com.example.yuxuanli.pinmoon.Matched;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yuxuanli.pinmoon.R;
import com.example.yuxuanli.pinmoon.Utils.TopNavigationViewHelper;
import com.example.yuxuanli.pinmoon.Utils.User;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;


public class Matched_Activity extends AppCompatActivity {

    private static final String TAG = "Matched_Activity";
    private static final int ACTIVITY_NUM = 2;

    private Context mContext = Matched_Activity.this;

    //test
    List<User> testList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);

        setupTopNavigationView();

        //test
        ProfileAdapter mAdapter = new ProfileAdapter(Matched_Activity.this, R.layout.matched_item, testList);
        ListView listView = (ListView) findViewById(R.id.matchList);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: The list has been clicked");
                checkClickedItem(position);
            }
        });
    }

    private void checkClickedItem(int position) {
        Intent intent = new Intent(this, ProfileCheckinMatched.class);
        intent.putExtra("name", testList.get(position).getUsername());
        intent.putExtra("email", testList.get(position).getEmail());
        intent.putExtra("sex", testList.get(position).getSex());
        intent.putExtra("number", testList.get(position).getPhone_number());
        //intent.putExtra("photo", testList.get(position).getProfile_photo() + "");
        startActivity(intent);
    }

    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = (BottomNavigationViewEx) findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
