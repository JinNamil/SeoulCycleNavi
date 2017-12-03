package com.example.user.viewpager_fragment.Activity;

import android.content.pm.ActivityInfo;

import com.example.user.viewpager_fragment.Fragment.Fragment5;
import com.github.clans.fab.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.viewpager_fragment.Fragment.Fragment1;
import com.example.user.viewpager_fragment.Fragment.Fragment2;
import com.example.user.viewpager_fragment.Fragment.Fragment3;
import com.example.user.viewpager_fragment.Fragment.Fragment4;
import com.example.user.viewpager_fragment.Item.BackPressCloseHandler;
import com.example.user.viewpager_fragment.R;

public class MainActivity extends AppCompatActivity {
    ViewPager pager;
    private BackPressCloseHandler backPressCloseHandler;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab_r, fab1, fab2, fab3;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
//
//    public void animateFAB() {
//
//        if (isFabOpen) {
//
//            fab_r.startAnimation(rotate_backward);
//            fab1.startAnimation(fab_close);
//            fab2.startAnimation(fab_close);
//            fab3.startAnimation(fab_close);
//            fab1.setClickable(false);
//            fab2.setClickable(false);
//            fab3.setClickable(false);
//            isFabOpen = false;
//            Log.d("Raj", "close");
//        } else {
//            fab_r.startAnimation(rotate_forward);
//            fab1.startAnimation(fab_open);
//            fab2.startAnimation(fab_open);
//            fab3.startAnimation(fab_open);
//            fab1.setClickable(true);
//            fab2.setClickable(true);
//            fab3.setClickable(true);
//            isFabOpen = true;
//            Log.d("Raj", "open");
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        pager = (ViewPager) findViewById(R.id.pager);
//        Button btn_first = (Button) findViewById(R.id.btn_first);
//        Button btn_second = (Button) findViewById(R.id.btn_second);
//        Button btn_third = (Button) findViewById(R.id.btn_third);
//        fab_r = (FloatingActionButton) findViewById(R.id.fab_r);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
//        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
//        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
//        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
//        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);

        pager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        pager.setCurrentItem(0);

        final View.OnClickListener movePageListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = (int) view.getTag();
                pager.setCurrentItem(tag);
                int id = view.getId();
                switch(id){
//                    case R.id.fab2:
//                        Toast.makeText(MainActivity.this, "(현재위치에서 대여소까지)\n\n보라색마커를 클릭해주세요", Toast.LENGTH_SHORT).show();
//                        break;
                    case R.id.fab1:
                        //Toast.makeText(MainActivity.this, "(현재위치에서 출발)\n\n검색지 혹은 대여소를 클릭해주세요", Toast.LENGTH_SHORT).show();
                }
//                switch (id) {
//                    case R.id.fab_r:
//                        animateFAB();
//                        break;
//                    case R.id.fab1:
//                        Log.d("Raj", "fab1");
//                        break;
//                    case R.id.fab2:
//                        Log.d("Raj", "fab2");
//                        break;
//                    case R.id.fab3:
//                        Log.d("Raj", "fab3");

            }
        };

        fab1.setOnClickListener(movePageListener);
//        fab1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "(현재위치에서 목적지까지)\n\n빨간색마커를 클릭해주세요", Toast.LENGTH_SHORT).show();
//                setContentView(R.layout.fragment_fragment1);
//            }
//        });
        fab1.setTag(0);
        fab2.setOnClickListener(movePageListener);
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "(현재위치에서 대여소까지)\n\n보라색마커를 클릭해주세요", Toast.LENGTH_SHORT).show();
//                setContentView(R.layout.fragment_fragment2);
//            }
//        });
        fab2.setTag(1);
        fab3.setOnClickListener(movePageListener);
//        fab3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "(목적지에서 대여소까지)\n\n보라색마커를 클릭해주세요", Toast.LENGTH_SHORT).show();
//                setContentView(R.layout.fragment_fragment3);
//            }
//        });
        fab3.setTag(2);
//        fab_r.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int id = v.getId();
//                switch (id) {
//                    case R.id.fab_r:
//                        animateFAB();
//                        break;
//                    case R.id.fab1:
//                        Log.d("Raj", "fab1");
//                        break;
//                    case R.id.fab2:
//                        Log.d("Raj", "fab2");
//                        break;
//                    case R.id.fab3:
//                        Log.d("Raj", "fab3");
//                }
//            }
//        });

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(FragmentManager fm) {

            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Fragment3();
                case 1:
                    return new Fragment5();
                case 2:
                    return new Fragment4();
                case 3:
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }
}
