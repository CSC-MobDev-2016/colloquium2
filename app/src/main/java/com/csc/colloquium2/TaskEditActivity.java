package com.csc.colloquium2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskEditActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String DATE_FORMAT = "EEE, d MMM yyyy HH:mm";

    private long taskId;
    private Task task;

    private EditText textET;
    private EditText titleET;
    private CheckBox favoriteCB;
    private CheckBox completedCB;
    private Button saveBTN;

    private EditText tagET;
    private Button addTagBtn;
    private ListView tagList;

    private TagCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_edit);
        taskId = getIntent().getLongExtra(MainActivity.TASK_ID_EXTRA, -1);

        textET = (EditText) findViewById(R.id.task_text_et);
        titleET = (EditText) findViewById(R.id.task_title_et);
        favoriteCB = (CheckBox) findViewById(R.id.favorite_cb);
        completedCB = (CheckBox) findViewById(R.id.completed_cb);
        saveBTN = (Button) findViewById(R.id.save_btn);
        tagET = (EditText) findViewById(R.id.tag_et);
        addTagBtn = (Button) findViewById(R.id.add_tag_btn);
        tagList = (ListView) findViewById(R.id.tags_list);

        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag();
            }
        });

        adapter = new TagCursorAdapter(this, null);
        tagList.setAdapter(adapter);

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskId == -1) {
                    addTask();
                } else {
                    updateTask();
                }
                finish();
            }
        });

        if (taskId != -1) {
            getSupportLoaderManager().initLoader(1, null, this);
            getSupportLoaderManager().initLoader(2, null, this);
        }
    }

    public void addTask() {
        ContentValues values = new ContentValues();
        SimpleDateFormat format =
                new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        values.put(
                TasksTable.COLUMN_TITLE,
                titleET.getText().toString()
        );
        values.put(
                TasksTable.COLUMN_TEXT,
                textET.getText().toString()
        );
        values.put(TasksTable.COLUMN_DATE,
                format.format(new Date()));
        values.put(TasksTable.COLUMN_COMPLETED,
                Boolean.toString(completedCB.isChecked()));
        values.put(TasksTable.COLUMN_FAVORITE,
                Boolean.toString(favoriteCB.isChecked()));
        getContentResolver().insert(
                MainActivity.TASKS_URI,
                values);
    }

    public void addTag() {
        ContentValues values = new ContentValues();
        values.put(
                TagsTable.COLUMN_TEXT,
                tagET.getText().toString()
        );
        values.put(
                TagsTable.COLUMN_TASK_ID,
                Long.toString(taskId)
        );
        getContentResolver().insert(
                MainActivity.TAGS_URI,
                values);
    }

    private void updateTask() {
        Uri uri = ContentUris.withAppendedId(MainActivity.TASKS_URI, taskId);
        ContentValues values = new ContentValues();
        SimpleDateFormat format =
                new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

        values.put(TasksTable.COLUMN_TEXT,
                textET.getText().toString());
        values.put(TasksTable.COLUMN_TITLE,
                titleET.getText().toString());
        values.put(TasksTable.COLUMN_DATE,
                format.format(new Date()));
        values.put(TasksTable.COLUMN_COMPLETED,
                Boolean.toString(completedCB.isChecked()));
        values.put(TasksTable.COLUMN_FAVORITE,
                Boolean.toString(favoriteCB.isChecked()));

        getContentResolver().update(uri, values, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cl = null;
        switch (id) {
            case 1:
            cl = new CursorLoader(
                    this,
                    MainActivity.TASKS_URI,
                    null,
                    "_ID = " + Long.toString(taskId),
                    null, null
            );
                break;
            case 2:
                cl = new CursorLoader(
                        this,
                        MainActivity.TAGS_URI,
                        null,
                        TagsTable.COLUMN_TASK_ID + " = " + Long.toString(taskId),
                        null, null
                );
                break;
        }
        return cl;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();
        if (id == 1) {

            TaskCursor taskCursor = new TaskCursor(data);
            taskCursor.moveToFirst();
            task = taskCursor.getTask();

            textET.setText(task.getText());
            titleET.setText(task.getTitle());
            favoriteCB.setChecked(task.isFavorite());
            completedCB.setChecked(task.isCompleted());
        }
        if (id == 2) {
            adapter.swapCursor(new TagCursor(data));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(new TagCursor(null));
    }
}
