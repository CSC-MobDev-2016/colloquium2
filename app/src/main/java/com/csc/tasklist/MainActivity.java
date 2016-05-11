package com.csc.tasklist;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    TaskAdapter taskAdapter;
    public static final Uri ENTRIES_URI = Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "entries");
    String[] data = { "Name", "Date" };
    int pos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView listTasks = (ListView) findViewById(R.id.list_tasks);
        taskAdapter = new TaskAdapter(this, null, this);
        listTasks.setAdapter(taskAdapter);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_very_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.header_alert));
                final LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.task_alert, null);
                builder.setView(ll);


                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues values = new ContentValues();
                        String header = ((EditText) ll.findViewById(R.id.alert_header)).getText().toString();
                        String body = ((EditText) ll.findViewById(R.id.alert_body)).getText().toString();
                        values.put(FeedsTable.COLUMN_HEADER, header);
                        values.put(FeedsTable.COLUMN_BODY, body);
                        values.put(FeedsTable.COLUMN_COLOR, Color.WHITE);
                        getContentResolver().insert(ENTRIES_URI, values);
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;
            case R.id.action_by_date:
                pos = 1;
                getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
                break;
            case R.id.action_by_name:
                pos = 0;
                getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ENTRIES_URI, null, null, null,   FeedsTable.COLUMN_DONE + ", " + FeedsTable.COLUMN_STAR + " DESC, " + (pos == 0 ? FeedsTable.COLUMN_HEADER : FeedsTable.COLUMN_DATE + " DESC"));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        taskAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        taskAdapter.swapCursor(null);
    }
}
