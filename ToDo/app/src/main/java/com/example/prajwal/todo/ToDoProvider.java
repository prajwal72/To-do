package com.example.prajwal.todo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class ToDoProvider extends ContentProvider {
    private DbHelper mDbHelper;
    public static final int TODO=100;
    public static final int TODO_ID=101;

    @Override
    public boolean onCreate() {

        mDbHelper=new DbHelper(getContext());
        return true;
    }

    public static UriMatcher todoUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static {
        todoUriMatcher.addURI(ToDoContract.CONTENT_AUTHORITY,ToDoContract.ToDoEntry.TABLE_NAME
                ,TODO);
        todoUriMatcher.addURI(ToDoContract.CONTENT_AUTHORITY,ToDoContract.ToDoEntry.TABLE_NAME+"/#",TODO_ID);

    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase dbase=mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match=todoUriMatcher.match(uri);

        if(match==TODO)
        {
            cursor=dbase.query(ToDoContract.ToDoEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);

        }
        else  if(match==TODO_ID)
        {
            selection=ToDoContract.ToDoEntry._ID+"=?";
            selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
            cursor=dbase.query(ToDoContract.ToDoEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        }
        else {
            throw new IllegalArgumentException("illegal uri");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        int match=todoUriMatcher.match(uri);
        if(match==TODO)
            return insertEntry(uri,contentValues);
        else
            throw new IllegalArgumentException("illegal uri");

    }

    private Uri insertEntry(Uri uri, ContentValues contentValues) {
        SQLiteDatabase dbase=mDbHelper.getWritableDatabase();
        String name=contentValues.getAsString(ToDoContract.ToDoEntry.COLUMN_TITLE);
        if(name==null){
            throw new IllegalArgumentException("illegal name");
        }
        Integer status=contentValues.getAsInteger(ToDoContract.ToDoEntry.STATUS);
        long rowId=dbase.insert(ToDoContract.ToDoEntry.TABLE_NAME,null,contentValues);
        getContext().getContentResolver().notifyChange(uri,null);
        Toast.makeText(getContext(), "To-Do saved ", Toast.LENGTH_SHORT).show();
        return ContentUris.withAppendedId(uri,rowId);


    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int match =todoUriMatcher.match(uri);
        if(contentValues.size()==0)
            return 0;
        SQLiteDatabase dbase=mDbHelper.getWritableDatabase();
        if(contentValues.containsKey(ToDoContract.ToDoEntry.COLUMN_TITLE)){

            String title=contentValues.getAsString(ToDoContract.ToDoEntry.COLUMN_TITLE);
            if(title==null){
                throw new IllegalArgumentException("illegal name");
            }
        }
        if(contentValues.containsKey(ToDoContract.ToDoEntry.COLUMN_CONTENT)){

            String content=contentValues.getAsString(ToDoContract.ToDoEntry.COLUMN_CONTENT);
            if(content==null){
                throw new IllegalArgumentException("illegal name");
            }
        }


        if(match==TODO){
            getContext().getContentResolver().notifyChange(uri,null);
            return dbase.update(ToDoContract.ToDoEntry.TABLE_NAME,contentValues,selection,selectionArgs);
        }
        else if(match==TODO_ID){
            selection=ToDoContract.ToDoEntry._ID+"=?";
            selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
            Log.d("sd","up");
            getContext().getContentResolver().notifyChange(uri,null);
            return dbase.update(ToDoContract.ToDoEntry.TABLE_NAME,contentValues,selection,selectionArgs);
        }
        else{
            throw new IllegalArgumentException("update not supportes");
        }

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match =todoUriMatcher.match(uri);
        SQLiteDatabase dbase=mDbHelper.getWritableDatabase();
        if(match==TODO){
            getContext().getContentResolver().notifyChange(uri,null);
            return dbase.delete(ToDoContract.ToDoEntry.TABLE_NAME,selection,selectionArgs);
        }
        else if(match==TODO_ID){
            getContext().getContentResolver().notifyChange(uri,null);
            selection=ToDoContract.ToDoEntry._ID+"=?";
            selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
            return dbase.delete(ToDoContract.ToDoEntry.TABLE_NAME,selection,selectionArgs);
        }
        else{
            throw new IllegalArgumentException("update not supportes");
        }
    }


    @Override
    public String getType(Uri uri) {
        final int match = todoUriMatcher.match(uri);
        switch (match) {
            case TODO:
                return ToDoContract.ToDoEntry.CONTENT_LIST_TYPE;
            case TODO_ID:
                return ToDoContract.ToDoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
