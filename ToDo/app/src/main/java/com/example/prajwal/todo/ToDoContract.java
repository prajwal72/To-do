package com.example.prajwal.todo;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ToDoContract {
    public static final String CONTENT_AUTHORITY = "com.example.prajwal.todo";

    public static class ToDoEntry implements BaseColumns {
        public static final String FINAL_URI="content://com.example.prajwal.todo/todo";
        public static final Uri URI=Uri.parse(FINAL_URI);
        public static final String TABLE_NAME="todo";
        public static final String _ID=BaseColumns._ID;
        public static final String COLUMN_TITLE="title";
        public static final String COLUMN_CONTENT="content";
        public static final String STATUS="status";
        public static final String CONTENT_LIST_TYPE=ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+TABLE_NAME;

        public static final int DONE =1;
        public static final int PENDING=0;
    }
}
