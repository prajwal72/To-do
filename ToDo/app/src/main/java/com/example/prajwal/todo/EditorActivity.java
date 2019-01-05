package com.example.prajwal.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.app.LoaderManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText mTitleEditText;
    private DbHelper mDbHelper;
    private EditText mContentEditText;
    private Uri uri;
    private int mStatus = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent=getIntent();
        uri=intent.getData();
        if(uri==null){
            setTitle("Add");
        }
        else{
            setTitle("Edit");
            getLoaderManager().initLoader(0,null,EditorActivity.this);
        }




        mTitleEditText = (EditText) findViewById(R.id.title);
        mContentEditText = (EditText) findViewById(R.id.content);
        mDbHelper=new DbHelper(this);
        final Button button2=findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mStatus==0){
                    mStatus=1;
                    button2.setText("PENDING");
                }
                else{
                    mStatus=0;
                    button2.setText("DONE");
                }

            }
        });
        Button del=findViewById(R.id.button3);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteList();
                finish();
            }
        });

        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                finish();
            }
        });
    }


    private void insertData() {
        ContentValues values = new ContentValues();
        String title=mTitleEditText.getText().toString().trim();

        String content =mContentEditText.getText().toString().trim();

        if(uri==null && title.isEmpty() && content.isEmpty())
            return;
        values.put(ToDoContract.ToDoEntry.COLUMN_TITLE, title);
        values.put(ToDoContract.ToDoEntry.COLUMN_CONTENT, content);
        values.put(ToDoContract.ToDoEntry.STATUS,mStatus);


        if(uri==null){


           getContentResolver().insert(ToDoContract.ToDoEntry.URI,values);

        }else{
            getContentResolver().update(uri,values,null,null);
            Log.d("sd","cr");
        }

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ToDoContract.ToDoEntry._ID,
                ToDoContract.ToDoEntry.COLUMN_TITLE,
                ToDoContract.ToDoEntry.STATUS,
                ToDoContract.ToDoEntry.COLUMN_CONTENT
        };
        return new CursorLoader(this,uri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToFirst()){
            int titleid=cursor.getColumnIndex(ToDoContract.ToDoEntry.COLUMN_TITLE);
            int contentid=cursor.getColumnIndex(ToDoContract.ToDoEntry.COLUMN_CONTENT);
            int statusid=cursor.getColumnIndex(ToDoContract.ToDoEntry.STATUS);
            String title=cursor.getString(titleid);
            String content=cursor.getString(contentid);
            int status=cursor.getInt(statusid);

            mTitleEditText.setText(title);
            mContentEditText.setText(content);
            Button button2=findViewById(R.id.button2);
            if(status==0){
                button2.setText("DONE");
            }
            else
            {
                button2.setText("PENDING");
            }
            mStatus=status;

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTitleEditText.setText("");
        mContentEditText.setText("");
    }

    private void deleteList() {
        if (uri != null) {
            int rowsDeleted = getContentResolver().delete(uri, null, null);
                Toast.makeText(this, "TO DO Deleted",
                        Toast.LENGTH_SHORT).show();

        }

    }
}
