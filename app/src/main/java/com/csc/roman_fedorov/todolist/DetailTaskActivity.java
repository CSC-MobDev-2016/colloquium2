package com.csc.roman_fedorov.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class DetailTaskActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TagsCursorAdapter adapter;
    private int taskNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        taskNumber = getIntent().getIntExtra(MainActivity.CLICKED_POSITION, 0) + 1;

        getSupportLoaderManager().initLoader(MainActivity.TODO_LOADER_ID, null, this);

        final EditText taskTitleEditText = (EditText) findViewById(R.id.detail_edit_title);
        final EditText taskDescriptionEditText = (EditText) findViewById(R.id.detail_edit_description);
        Button updateButton = (Button) findViewById(R.id.detail_button_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = taskTitleEditText.getText().toString();
                String taskDescription = taskDescriptionEditText.getText().toString();
                ContentValues mUpdateValues = new ContentValues();
                mUpdateValues.put(ToDoTable.COLUMN_TITLE, taskTitle);
                mUpdateValues.put(ToDoTable.COLUMN_DESCRIPTION, taskDescription);
                getContentResolver().update(MainActivity.ENTRIES_URI, mUpdateValues, " _ID = ? ",
                        new String[]{String.valueOf(taskNumber)});

                Intent intent = new Intent(DetailTaskActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Cursor cursor = getContentResolver().query(MainActivity.ENTRIES_URI, null, " _ID = ? ",
                new String[]{String.valueOf(taskNumber)}, null);
        cursor.moveToNext();

        String title = cursor.getString(cursor.getColumnIndex(ToDoTable.COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(ToDoTable.COLUMN_DESCRIPTION));
        taskTitleEditText.setText(title);
        taskDescriptionEditText.setText(description);
        cursor.close();

        adapter = new TagsCursorAdapter(this, null, 0);
        ListView tagsListView = (ListView) findViewById(R.id.tags_listview);
        tagsListView.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CreateNewTaskActivity.TAGS_URI, null,
                " " + TagsTable.COLUMN_TASK_ID + " = ? ", new String[]{String.valueOf(taskNumber)}, null);
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
