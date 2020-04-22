package com.example.collegeconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
    private static final String PREF_USER_NAME= "username";
    private static final String USER = "user";
    private static final String TT="Timetable";
    private static final String COURSE = "course";
    private static final String BRANCH = "branch";
    private static final String SEMESTER = "semester";
    private static final String UNIT = "unit";

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
    public static void setTt(Context ctx, String path)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(TT, path);
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

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getUser(Context ctx)
    {
        return  getSharedPreferences(ctx).getString(USER, "");
    }
    public static String getTt(Context ctx)
    {
        return getSharedPreferences(ctx).getString(TT, "");
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


    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}
