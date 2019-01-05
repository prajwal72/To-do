package com.example.prajwal.todo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TodoAdapter extends CursorAdapter {

    public TodoAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView titleView=(TextView)view.findViewById(R.id.titleView);

        String title=cursor.getString(cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.COLUMN_TITLE));
        /*int id=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry._ID));
        String ids=Integer.toString(id);*/
        ImageView imgView=(ImageView)view.findViewById(R.id.image);
        int status=cursor.getInt(cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.STATUS));
        if(status==1){
            imgView.setImageResource(R.drawable.ic_check_green_24dp);
        }
        else
            imgView.setImageResource(R.drawable.ic_error_yellow_24dp);
        titleView.setText(title);



    }
}
