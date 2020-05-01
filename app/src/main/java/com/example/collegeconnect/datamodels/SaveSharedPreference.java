package com.example.collegeconnect.datamodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    private static final String PREF_USER_NAME= "username";
    private static final String USER = "user";
    private static final String CHECKED_ITEM="checked_item";
    private static final String COURSE = "course";
    private static final String BRANCH = "branch";
    private static final String SEMESTER = "semester";
    private static final String UNIT = "unit";
    private static final String CLEARALL = "clearall";
    private static final String CLEARALL1 = "clearall1";

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }
    public static void setUser(Context ctx, String user)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER, user);
        editor.commit();
    }
    public static void setCheckedItem(Context ctx, int path)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(CHECKED_ITEM, path);
        editor.commit();
    }
    public static void setCourse(Context ctx, int num)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(COURSE, num);
        editor.commit();
    }
    public static void setBranch(Context ctx, int num)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(BRANCH, num);
        editor.commit();
    }
    public static void setSemester(Context ctx, int num)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(SEMESTER, num);
        editor.commit();
    }
    public static void setUnit(Context ctx, int num)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(UNIT, num);
        editor.commit();
    }
    public static void setClearall(Context ctx , boolean value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(CLEARALL,value);
        editor.commit();
    }
    public static void setClearall1(Context ctx , boolean value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(CLEARALL1,value);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getUser(Context ctx)
    {
        return  getSharedPreferences(ctx).getString(USER, "");
    }
    public static int getCheckedItem(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(CHECKED_ITEM, 0);
    }
    public static int getCourse(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(COURSE,0);
    }
    public static int getBranch(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(BRANCH,0);
    }
    public static int getSemester(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(SEMESTER,0);
    }
    public static int getUnit(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(UNIT,0);
    }
    public static  boolean getClearall(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(CLEARALL,false);
    }
    public static  boolean getClearall1(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(CLEARALL1,false);
    }


    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}
