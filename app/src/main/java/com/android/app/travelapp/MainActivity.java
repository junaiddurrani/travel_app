package com.android.app.travelapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager viewPager;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;

    private int[] tabIcons = {R.drawable.home, R.drawable.tour,
            R.drawable.explore, R.drawable.person};

    private int[] tabs_not_select = {R.drawable.hom_not_select, R.drawable.tour_not_select, R.drawable.explore_not_select
    , R.drawable.person_not_select};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);

        tabs.setupWithViewPager(viewPager);

        setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                changeTabs(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tabsIcon();
    }

    private void tabsIcon() {
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabs_not_select[1]);
        tabs.getTabAt(2).setIcon(tabs_not_select[2]);
        tabs.getTabAt(3).setIcon(tabs_not_select[3]);
    }

    private void changeTabs(int i) {
        if (i==0) {
            tabs.getTabAt(0).setIcon(tabIcons[0]);
            tabs.getTabAt(1).setIcon(tabs_not_select[1]);
            tabs.getTabAt(2).setIcon(tabs_not_select[2]);
            tabs.getTabAt(3).setIcon(tabs_not_select[3]);
        }
        if (i==1) {
            tabs.getTabAt(0).setIcon(tabs_not_select[0]);
            tabs.getTabAt(1).setIcon(tabIcons[1]);
            tabs.getTabAt(2).setIcon(tabs_not_select[2]);
            tabs.getTabAt(3).setIcon(tabs_not_select[3]);
        }
        if (i==2) {
            tabs.getTabAt(0).setIcon(tabs_not_select[0]);
            tabs.getTabAt(1).setIcon(tabs_not_select[1]);
            tabs.getTabAt(2).setIcon(tabIcons[2]);
            tabs.getTabAt(3).setIcon(tabs_not_select[3]);
        }
        if (i==3) {
            tabs.getTabAt(0).setIcon(tabs_not_select[0]);
            tabs.getTabAt(1).setIcon(tabs_not_select[1]);
            tabs.getTabAt(2).setIcon(tabs_not_select[2]);
            tabs.getTabAt(3).setIcon(tabIcons[3]);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            sendTologin();
        }
    }

    private void sendTologin() {
        startActivity(new Intent(MainActivity.this, WelcomeScreen.class));
        finish();
    }

    private void setupWithViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Home());
        adapter.addFrag(new Tour());
        adapter.addFrag(new Explore());
        adapter.addFrag(new Profile());
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        public void addFrag(Fragment name) {
            fragmentList.add(name);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addPost) {
            startActivity(new Intent(MainActivity.this, AddPost.class));
        }
        if (item.getItemId() == R.id.addTour) {
            startActivity(new Intent(MainActivity.this, AddTour.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
