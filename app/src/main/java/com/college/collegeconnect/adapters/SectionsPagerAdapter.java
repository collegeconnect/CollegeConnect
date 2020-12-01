package com.college.collegeconnect.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.college.collegeconnect.timetable.NewTimeTableViewModel;
import com.college.collegeconnect.timetable.PlaceholderFragment;

import org.jetbrains.annotations.NotNull;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private NewTimeTableViewModel newTimeTableViewModel;
    public static final String[] TAB_TITLES = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};

    public SectionsPagerAdapter(FragmentManager fm, NewTimeTableViewModel newTimeTableViewModel) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.newTimeTableViewModel = newTimeTableViewModel;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return new PlaceholderFragment(position, newTimeTableViewModel);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        // Show 7 total pages.
        return 7;
    }
}