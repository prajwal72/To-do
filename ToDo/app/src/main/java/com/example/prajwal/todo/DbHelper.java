package com.example.prajwal.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "list.db";
    private static final int DATABASE_VERSION = 1;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_TODO_TABLE =  "CREATE TABLE " + ToDoContract.ToDoEntry.TABLE_NAME + " ("
                + ToDoContract.ToDoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ToDoContract.ToDoEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + ToDoContract.ToDoEntry.STATUS + " INTEGER, "
                + ToDoContract.ToDoEntry.COLUMN_CONTENT + " TEXT);";
        db.execSQL(SQL_CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
