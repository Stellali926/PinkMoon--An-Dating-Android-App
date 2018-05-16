package com.example.yuxuanli.pinmoon.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.yuxuanli.pinmoon.Main.MainActivity;
import com.example.yuxuanli.pinmoon.Matched.Matched_Activity;
import com.example.yuxuanli.pinmoon.Profile.Profile_Activity;
import com.example.yuxuanli.pinmoon.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by yuxuanli on 5/9/18.
 */

public class TopNavigationViewHelper {

    private static final String TAG = "TopNavigationViewHelper";

    public static void setupTopNavigationView(BottomNavigationViewEx tv) {
        Log.d(TAG, "setupTopNavigationView: setting up navigationview");

        tv.enableAnimation(false);
        tv.enableItemShiftingMode(false);
        tv.enableShiftingMode(false);
        tv.setTextVisibility(false);
        tv.setIconSize(35, 35);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_profile:
                        Intent intent2 = new Intent(context, Profile_Activity.class);
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_main:
                        Intent intent1 = new Intent(context, MainActivity.class);
                        context.startActivity(intent1);
                        break;

                    case R.id.ic_matched:
                        Intent intent3 = new Intent(context, Matched_Activity.class);
                        context.startActivity(intent3);

                        break;
                }

                return false;
            }
        });
    }
}
