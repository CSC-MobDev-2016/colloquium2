package com.csc.simple_todo_list;

import android.content.*;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import static com.csc.simple_todo_list.ToDoTable.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = "MainActivity";

    public static final Uri ENTRIES_URI = Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "entries");
    private MyListCursorAdapter adapter;
    private RecyclerView recyclerView;

    private final ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(TAG, "ContentObserver: onChange");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //onUpdate(view);
                    }
                })
        );*/

        Cursor cursor = managedQuery(ENTRIES_URI,  null, null, null, null);
        adapter = new MyListCursorAdapter(this, cursor);
        recyclerView.setAdapter(adapter);
        cursor.registerContentObserver(observer);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(observer);
    }

    public void addData(View view) {

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("TASK_TITLE", "_new");
        startActivity(intent);
        /*final EditText editText = new EditText(this.getApplicationContext());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        final String dateString = sdf.format(System.currentTimeMillis());
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Create task")
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ContentValues values = new ContentValues();
                        values.put(COLUMN_TITLE, editText.getText().toString());
                        values.put(COLUMN_DATE, dateString);
                        values.put(COLUMN_STARRED, 0);
                        values.put(COLUMN_DONE, 0);
                        getContentResolver().insert(ENTRIES_URI,values);
                        getSupportLoaderManager().getLoader(0).forceLoad();
                    }})
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {}});
        builder.create().show();*/
    }
    public void showData(String task) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("TASK_TITLE", task);
        startActivity(intent);
    }

    public void updateTask(int pos, String title, String date, boolean isDone, boolean isStarred) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_STARRED, isStarred ? 1 : 0);
        values.put(COLUMN_DONE, isDone ? 1 : 0);
        getContentResolver().update(ENTRIES_URI, values, "_id = " + pos, null);
        getSupportLoaderManager().getLoader(0).forceLoad();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, getContentResolver());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {

        ContentResolver res;

        public MyCursorLoader(Context context, ContentResolver res) {
            super(context);
            this.res = res;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = res.query(ENTRIES_URI, null, null, null, null);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }

    }

}
