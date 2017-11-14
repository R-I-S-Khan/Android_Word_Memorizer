package com.risk.dolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.risk.dolist.TaskCauseItem;

import java.util.ArrayList;

/**
 * Created by redwan on 7/30/17.
 */

public class DBhelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "task_cause_database";
    public static String TABLE_NAME = "task_cause_table";
    public static String TASK_NAME = "_task";
    public static String TASK_CAUSE = "_cause";
    public static String ROW_ID = "_id";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TASK_NAME + " TEXT, " + TASK_CAUSE + " TEXT)";
        db.execSQL(query);

    }

    public void insert(TaskCauseItem item){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv  =  new ContentValues();
        cv.put(TASK_NAME,item.task);
        cv.put(TASK_CAUSE,item.cause);
        db.insert(TABLE_NAME,null,cv);
    }
    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,ROW_ID + "=" + id,null );

    }

    public ArrayList<TaskCauseItem> retrieve(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + TABLE_NAME;
        Cursor c = db.rawQuery(query,null);
        return convert(c);

    }
    private ArrayList<TaskCauseItem> convert(Cursor c){
        c.moveToFirst();
        ArrayList<TaskCauseItem> temp = new ArrayList<>();

        while(c.isAfterLast() == false){

            int id = c.getInt(c.getColumnIndex(ROW_ID));
            String task = c.getString(c.getColumnIndex(TASK_NAME));
            String cause = c.getString(c.getColumnIndex(TASK_CAUSE));

            temp.add(new TaskCauseItem(id,task,cause));
            c.moveToNext();
        }
        return temp;


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
