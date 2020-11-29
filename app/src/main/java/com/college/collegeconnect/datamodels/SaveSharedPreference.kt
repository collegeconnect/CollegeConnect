package com.college.collegeconnect.datamodels

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object SaveSharedPreference {
    private const val PREF_USER_NAME = "username"
    private const val USER = "user"
    private const val CHECKED_ITEM = "checked_item"
    private const val COURSE = "course"
    private const val BRANCH = "branch"
    private const val SEMESTER = "semester"
    private const val UNIT = "unit"
    private const val CLEARALL = "clearall"
    private const val CLEARALL1 = "clearall1"
    private const val REF = "com.connect.collegeconnect.MyPref"
    private const val ATTENDANCE_CRITERIA = "attendance_criteria"
    private const val POP = "pop"
    private const val DETAILS_UPLOADED = "uploaded"
    private const val COLLEGE = "college"
    private const val REVIEW = "review"
    private fun getSharedPreferences(ctx: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    // To store upload status
    @JvmStatic
    fun setUploaded(ctx: Context?, `var`: Boolean?) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putBoolean(DETAILS_UPLOADED, `var`!!)
        editor.apply()
    }

    // To store email
    @JvmStatic
    fun setUserName(ctx: Context?, userName: String?) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(PREF_USER_NAME, userName)
        editor.apply()
    }

    // To store name
    @JvmStatic
    fun setUser(ctx: Context?, user: String?) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(USER, user)
        editor.apply()
    }

    //For storing theme choice
    @JvmStatic
    fun setCheckedItem(ctx: Context?, path: Int) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putInt(CHECKED_ITEM, path)
        editor.apply()
    }

    @JvmStatic
    fun setCourse(ctx: Context?, num: Int) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putInt(COURSE, num)
        editor.apply()
    }

    @JvmStatic
    fun setBranch(ctx: Context?, num: Int) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putInt(BRANCH, num)
        editor.apply()
    }

    @JvmStatic
    fun setSemester(ctx: Context?, num: Int) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putInt(SEMESTER, num)
        editor.apply()
    }

    @JvmStatic
    fun setUnit(ctx: Context?, num: Int) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putInt(UNIT, num)
        editor.apply()
    }

    @JvmStatic
    fun setClearall(ctx: Context?, value: Boolean) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putBoolean(CLEARALL, value)
        editor.apply()
    }

    @JvmStatic
    fun setClearall1(ctx: Context?, value: Boolean) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putBoolean(CLEARALL1, value)
        editor.apply()
    }

    //For on boarding screen
    @JvmStatic
    fun setRef(ctx: Context?, value: Boolean) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putBoolean(REF, value)
        editor.apply()
    }

    @JvmStatic
    fun setAttendanceCriteria(ctx: Context?, attendance_criteria: Int) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putInt(ATTENDANCE_CRITERIA, attendance_criteria)
        editor.apply()
    }

    @JvmStatic
    fun setPop(ctx: Context?, pop: Int) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putInt(POP, pop)
        editor.apply()
    }

    @JvmStatic
    fun setCollege(ctx: Context?, college: String?) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(COLLEGE, college)
        editor.apply()
    }

    @JvmStatic
    fun setRev(ctx: Context?, value: Boolean) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putBoolean(REVIEW, value)
        editor.apply()
    }

    fun getAttendanceCriteria(ctx: Context?): Int {
        return getSharedPreferences(ctx).getInt(ATTENDANCE_CRITERIA, 75)
    }

    @JvmStatic
    fun getUserName(ctx: Context?): String? {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "")
    }

    @JvmStatic
    fun getUser(ctx: Context?): String? {
        return getSharedPreferences(ctx).getString(USER, "")
    }

    @JvmStatic
    fun getCheckedItem(ctx: Context?): Int {
        return getSharedPreferences(ctx).getInt(CHECKED_ITEM, 2)
    }

    @JvmStatic
    fun getCourse(ctx: Context?): Int {
        return getSharedPreferences(ctx).getInt(COURSE, 0)
    }

    @JvmStatic
    fun getBranch(ctx: Context?): Int {
        return getSharedPreferences(ctx).getInt(BRANCH, 0)
    }

    @JvmStatic
    fun getSemester(ctx: Context?): Int {
        return getSharedPreferences(ctx).getInt(SEMESTER, 0)
    }

    @JvmStatic
    fun getUnit(ctx: Context?): Int {
        return getSharedPreferences(ctx).getInt(UNIT, 0)
    }

    @JvmStatic
    fun getClearall(ctx: Context?): Boolean {
        return getSharedPreferences(ctx).getBoolean(CLEARALL, false)
    }

    fun getClearall1(ctx: Context?): Boolean {
        return getSharedPreferences(ctx).getBoolean(CLEARALL1, false)
    }

    @JvmStatic
    fun getRef(ctx: Context?): Boolean {
        return getSharedPreferences(ctx).getBoolean(REF, false)
    }

    @JvmStatic
    fun getUpload(ctx: Context?): Boolean {
        return getSharedPreferences(ctx).getBoolean(DETAILS_UPLOADED, false)
    }

    @JvmStatic
    fun getPop(ctx: Context?): Int {
        return getSharedPreferences(ctx).getInt(POP, 1)
    }

    @JvmStatic
    fun getCollege(ctx: Context?): String? {
        return getSharedPreferences(ctx).getString(COLLEGE, "other")
    }

    @JvmStatic
    fun getReview(ctx: Context?): Boolean {
        return getSharedPreferences(ctx).getBoolean(REVIEW, false)
    }

    //Clear data on logout
    @JvmStatic
    fun clearUserName(ctx: Context?) {
        val editor = getSharedPreferences(ctx).edit()
        editor.remove(PREF_USER_NAME)
        editor.remove(USER)
        editor.remove(CHECKED_ITEM)
        editor.remove(COURSE)
        editor.remove(BRANCH)
        editor.remove(SEMESTER)
        editor.remove(CLEARALL1)
        editor.remove(CLEARALL)
        editor.remove(UNIT)
        editor.remove(ATTENDANCE_CRITERIA)
        editor.remove(REF)
        editor.remove(COLLEGE)
        editor.apply()
    }
}