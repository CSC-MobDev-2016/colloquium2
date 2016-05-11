package com.csc.colloquium2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TASK_ID_EXTRA =
            "ru.ppzh.dotolist.task_id";


    public static final String SORT_ORDER = TasksTable.COLUMN_COMPLETED + ", "
            + TasksTable.COLUMN_FAVORITE + " DESC, "
            + TasksTable.COLUMN_DATE + " DESC";

    public static final Uri TASKS_URI = Uri.withAppendedPath(ToDoListContentProvider.CONTENT_URI, "tasks");
    public static final Uri TAGS_URI = Uri.withAppendedPath(ToDoListContentProvider.CONTENT_URI, "tags");

    private TaskCursorAdapter adapter;

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        adapter = new TaskCursorAdapter(this, null);

        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), TaskEditActivity.class);
                i.putExtra(TASK_ID_EXTRA, id);
                startActivity(i);
            }
        });

        getSupportLoaderManager().initLoader(0, null, this);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, TASKS_URI, null, null, null, SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(new TaskCursor(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.task_add:
                Intent i = new Intent(this, TaskEditActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
