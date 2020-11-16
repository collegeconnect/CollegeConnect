package com.college.collegeconnect.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.college.collegeconnect.R;
import com.college.collegeconnect.adapters.ViewPagerAdapter;
import com.college.collegeconnect.datamodels.SaveSharedPreference;
import com.google.android.material.tabs.TabLayout;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class OnBoardingScreen extends AppCompatActivity {

    private int[] layouts = {R.layout.first_slide, R.layout.second_slide, R.layout.third_slide};
    private Button back, next;
    private int currentPage;
    private WormDotsIndicator wormDotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SaveSharedPreference.getRef(this)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_on_boarding_screenm);

        final ViewPager pager = findViewById(R.id.viewPager);
        PagerAdapter adapter = new ViewPagerAdapter(layouts, OnBoardingScreen.this);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                createDots(position);
                currentPage = position;
                if (currentPage == 0) {
                    back.setEnabled(false);
                    next.setEnabled(true);
                    back.setVisibility(View.INVISIBLE);
                } else if (currentPage == layouts.length - 1) {
                    back.setEnabled(true);
                    next.setEnabled(true);
                    back.setVisibility(View.VISIBLE);
                    next.setText("FINISH");
                } else {
                    back.setEnabled(true);
                    next.setEnabled(true);
                    back.setVisibility(View.VISIBLE);
                    next.setText("NEXT");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        wormDotsIndicator = findViewById(R.id.worm_dots_indicator);
        back = findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(currentPage - 1);
            }
        });

        next = findViewById(R.id.buttonNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPage == layouts.length - 1) {
                    SaveSharedPreference.setRef(OnBoardingScreen.this, true);
                    startActivity(new Intent(OnBoardingScreen.this, MainActivity.class));
                    finish();
                } else
                    pager.setCurrentItem(currentPage + 1);
            }
        });
        wormDotsIndicator.setViewPager(pager);
//        TabLayout tabLayout = findViewById(R.id.tab_layout);
//        tabLayout.setupWithViewPager(pager, true);

    }
}
