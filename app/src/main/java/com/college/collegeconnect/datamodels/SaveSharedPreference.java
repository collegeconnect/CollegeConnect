package com.college.collegeconnect.datamodels;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SaveSharedPreference {
    private static final String PREF_USER_NAME = "username";
    private static final String USER = "user";
    private static final String CHECKED_ITEM = "checked_item";
    private static final String COURSE = "course";
    private static final String BRANCH = "branch";
    private static final String SEMESTER = "semester";
    private static final String UNIT = "unit";
    private static final String CLEARALL = "clearall";
    private static final String CLEARALL1 = "clearall1";
    private static final String REF = "com.connect.collegeconnect.MyPref";
    private static final String ATTENDANCE_CRITERIA = "attendance_criteria";
    private static final String POP = "pop";
    private static final String DETAILS_UPLOADED = "uploaded";

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // To store upload status
    public static void setUploaded(Context ctx, Boolean var) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(DETAILS_UPLOADED, var);
        editor.apply();
    }

    // To store email
    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.apply();
    }

    // To store name
    public static void setUser(Context ctx, String user) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER, user);
        editor.apply();
    }

    //For storing theme choice
    public static void setCheckedItem(Context ctx, int path) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(CHECKED_ITEM, path);
        editor.apply();
    }

    public static void setCourse(Context ctx, int num) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(COURSE, num);
        editor.apply();
    }

    public static void setBranch(Context ctx, int num) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(BRANCH, num);
        editor.apply();
    }

    public static void setSemester(Context ctx, int num) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(SEMESTER, num);
        editor.apply();
    }

    public static void setUnit(Context ctx, int num) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(UNIT, num);
        editor.apply();
    }

    public static void setClearall(Context ctx, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(CLEARALL, value);
        editor.apply();
    }

    public static void setClearall1(Context ctx, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(CLEARALL1, value);
        editor.apply();
    }

    //For on boarding screen
    public static void setRef(Context ctx, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(REF, value);
        editor.apply();
    }

    public static void setAttendanceCriteria(Context ctx, int attendance_criteria) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(ATTENDANCE_CRITERIA, attendance_criteria);
        editor.apply();
    }

    public static void setPop(Context ctx, int pop) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(POP, pop);
        editor.apply();
    }

    public static int getAttendanceCriteria(Context ctx) {
        return getSharedPreferences(ctx).getInt(ATTENDANCE_CRITERIA, 75);
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getUser(Context ctx) {
        return getSharedPreferences(ctx).getString(USER, "");
    }

    public static int getCheckedItem(Context ctx) {
        return getSharedPreferences(ctx).getInt(CHECKED_ITEM, 0);
    }

    public static int getCourse(Context ctx) {
        return getSharedPreferences(ctx).getInt(COURSE, 0);
    }

    public static int getBranch(Context ctx) {
        return getSharedPreferences(ctx).getInt(BRANCH, 0);
    }

    public static int getSemester(Context ctx) {
        return getSharedPreferences(ctx).getInt(SEMESTER, 0);
    }

    public static int getUnit(Context ctx) {
        return getSharedPreferences(ctx).getInt(UNIT, 0);
    }

    public static boolean getClearall(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(CLEARALL, false);
    }

    public static boolean getClearall1(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(CLEARALL1, false);
    }

    public static boolean getRef(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(REF, false);
    }

    public static boolean getUpload(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(DETAILS_UPLOADED, false);
    }

    public static int getPop(Context ctx) {
        return getSharedPreferences(ctx).getInt(POP, 1);
    }

    //Clear data on logout
    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_USER_NAME);
        editor.remove(USER);
        editor.remove(CHECKED_ITEM);
        editor.remove(COURSE);
        editor.remove(BRANCH);
        editor.remove(SEMESTER);
        editor.remove(CLEARALL1);
        editor.remove(CLEARALL);
        editor.remove(UNIT);
        editor.remove(ATTENDANCE_CRITERIA);
        editor.apply();
    }

}
