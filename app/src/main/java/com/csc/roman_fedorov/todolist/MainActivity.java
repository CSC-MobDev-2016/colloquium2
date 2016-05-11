package com.csc.roman_fedorov.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String CLICKED_POSITION = "CLICKED_POSITION";

    public static final Uri ENTRIES_URI = Uri.withAppendedPath(ToDoTasksContentProvider.CONTENT_URI, "entries");

    private MyCursorAdapter adapter;

    public static final int TODO_LOADER_ID = 42;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(MainActivity.this, CreateNewTaskActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getSupportLoaderManager().initLoader(TODO_LOADER_ID, null, this);

        final ListView todoLv = (ListView) findViewById(R.id.todo_list);
        todoLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailTaskActivity.class);
                intent.putExtra(CLICKED_POSITION, position);
                startActivity(intent);
            }
        });

        adapter = new MyCursorAdapter(this, null, 0);
        todoLv.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        return new CursorLoader(this, ENTRIES_URI, null, null, null, ToDoTable.COLUMN_TITLE + " DESC");
        return new CursorLoader(this, ENTRIES_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
