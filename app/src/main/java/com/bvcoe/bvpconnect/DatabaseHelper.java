package com.bvcoe.bvpconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TableLayout;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String Database_name= "Attendance.db";
    public static final String Table_name= "Attendance_Table";
    public static final String Col1= "ID";
    public static final String Col2= "Name";
    public static final String Col3= "Attended";
    public static final String Col4= "Missed";

    public DatabaseHelper(@Nullable Context context) {
        super(context, Database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+Table_name+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, ATTENDED INTEGER, MISSED INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Table_name);
        onCreate(sqLiteDatabase);
    }

    public boolean insetData(String name, String attended,String missed){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col2,name);
        contentValues.put(Col3,attended);
        contentValues.put(Col4,missed);
        long result=db.insert(Table_name,null,contentValues);
        if (result==-1)
            return false;
        return true;
    }

    public Cursor viewAllData() {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+Table_name,null);
        return res;
    }

    public boolean updateData(String name, String attended, String missed){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col2,name);
        contentValues.put(Col3,attended);
        contentValues.put(Col4,missed);
        db.update(Table_name,contentValues,"NAME = ?",new String[] {name});
        return true;
    }

    public int deleteData(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(Table_name,"NAME = ?",new String[] {name});

    }
}
