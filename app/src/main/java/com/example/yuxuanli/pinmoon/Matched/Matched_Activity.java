package com.example.yuxuanli.pinmoon.Matched;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        initAnimal();
        ProfileAdapter mAdapter = new ProfileAdapter(Matched_Activity.this, R.layout.matched_item, testList);
        ListView listView = (ListView) findViewById(R.id.matchList);
        listView.setAdapter(mAdapter);
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

    private void initAnimal() {
        User monkey = new User("monkey", "4099991919", "sdsd@gmail.com", "Mr.Monkey", R.drawable.monkey,new ArrayList<String>(),"");
        testList.add(monkey);

        User lion = new User("lion", "4099991919", "sdsd@gmail.com", "Mr.Lion", R.drawable.lion,new ArrayList<String>(),"");
        testList.add(lion);

        User cat = new User("cat", "4099991919", "sdsd@gmail.com", "Ms.Cat", R.drawable.cat,new ArrayList<String>(),"");
        testList.add(cat);

        User snake = new User("monkey", "4099991919", "sdsd@gmail.com", "Mr.Snake", R.drawable.snake,new ArrayList<String>(),"");
        testList.add(snake);

        User monkey1 = new User("monkey", "4099991919", "sdsd@gmail.com", "Mr.Monkey", R.drawable.monkey,new ArrayList<String>(),"");
        testList.add(monkey1);

        User lion2 = new User("lion", "4099991919", "sdsd@gmail.com", "Mr.Lion", R.drawable.lion,new ArrayList<String>(),"");
        testList.add(lion2);

        User cat2 = new User("cat", "4099991919", "sdsd@gmail.com", "Ms.Cat", R.drawable.cat,new ArrayList<String>(),"");
        testList.add(cat2);

        User snake3 = new User("monkey", "4099991919", "sdsd@gmail.com", "Mr.Snake", R.drawable.snake,new ArrayList<String>(),"");
        testList.add(snake3);


    }
}
