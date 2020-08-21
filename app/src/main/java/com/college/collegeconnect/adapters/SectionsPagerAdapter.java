package com.college.collegeconnect.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.college.collegeconnect.ui.PlaceholderFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public static final String[] TAB_TITLES = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return new PlaceholderFragment(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        // Show 6 total pages.
        return 6;
    }
}