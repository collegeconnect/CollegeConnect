package com.college.collegeconnect.datamodels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String Database_name = "Attendance.db";
    public static final String Table_name = "Attendance_Table";
    public static final String Tablename = "Image_table";
    public static final String Col1 = "ID";
    public static final String Col2 = "Name";
    public static final String Col3 = "Attended";
    public static final String Col4 = "Missed";
    public static final String COL = "Imagetext";
    public static final String COL1 = "Imageblob";
    public static final String string = "image";

    public DatabaseHelper(@Nullable Context context) {
        super(context, Database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + Table_name + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, ATTENDED INTEGER, MISSED INTEGER)");
        sqLiteDatabase.execSQL("create table " + Tablename + " (IMAGETEXT TEXT PRIMARY KEY, IMAGEBLOB BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_name);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tablename);
        onCreate(sqLiteDatabase);
    }

    public boolean insetData(String name, String attended, String missed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col2, name);
        contentValues.put(Col3, attended);
        contentValues.put(Col4, missed);
        long result = db.insert(Table_name, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }

    public boolean insertImage(byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL, string);
        cv.put(COL1, image);
        long res = db.insert(Tablename, null, cv);
        return res != -1;
    }

    public Cursor viewAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + Table_name, null);
        return res;
    }

    public Cursor viewAllImage() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + Tablename, null);
        return res;
    }

    public boolean updateData(String ID, String name, String attended, String missed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col1, ID);
        contentValues.put(Col2, name);
        contentValues.put(Col3, attended);
        contentValues.put(Col4, missed);
        db.update(Table_name, contentValues, "NAME = ?", new String[]{name});
        return true;
    }

    public boolean updateImage(byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL, string);
        cv.put(COL1, image);
        db.update(Tablename, cv, "IMAGETEXT = ?", new String[]{string});
        return true;
    }

    public int deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Table_name, "NAME = ?", new String[]{name});

    }

    public Cursor getClasses(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String args = Col2 + " LIKE ?";
        return db.query(Table_name, new String[]{Col3, Col4}, args, new String[]{name}, null, null, null);
    }

    public void deleteall() {
        SQLiteDatabase db = this.getWritableDatabase();
        // If whereClause is null, it will delete all rows.
        db.delete(Table_name, null, null);
        db.delete(Tablename, null, null);
    }

    public Cursor getTotal() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(Table_name, new String[]{Col3, Col4}, null, null, null, null, null);
    }

    public String calculateTotal() {
        Cursor res = getTotal();
        int totalAttended = 0, totalMissed = 0;
        while (res.moveToNext()) {
            totalAttended += Integer.parseInt(res.getString(0));
            totalMissed += Integer.parseInt(res.getString(1));
        }
        String percentage = String.format("%.2f", (float) totalAttended / (totalAttended + totalMissed) * 100);
        return percentage;
    }
}
