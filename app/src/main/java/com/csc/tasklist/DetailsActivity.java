package com.csc.tasklist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Oleg Doronin
 * colloquium2
 * Copyright (c) 2016 CS. All rights reserved.
 */
public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final Uri ENTRIES_TAGS = Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "tags");
    private String taskId;
    private TagAdapter tagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        final String taskId = getIntent().getStringExtra(GlobalContext.TASK_ID);
        this.taskId = taskId;
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(MainActivity.ENTRIES_URI, null, FeedsTable._ID + "=" + taskId, null, null);
            if (cursor.moveToNext()) {
                String header = cursor.getString(cursor.getColumnIndex(FeedsTable.COLUMN_HEADER));
                String body = cursor.getString(cursor.getColumnIndex(FeedsTable.COLUMN_BODY));
                TextView tvHeader = (TextView) findViewById(R.id.details_header);
                TextView tvBody = (TextView) findViewById(R.id.details_body);
                tvHeader.setText(header);
                tvBody.setText(body);
            }
        } catch (Exception ex) {
            if (cursor != null) {
                cursor.close();
            }
        }

        Button btnAddTag = (Button) findViewById(R.id.details_tag_add);
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setTitle(getString(R.string.header_tag_alert));
                final LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.tag_alert, null);
                builder.setView(ll);

                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues values = new ContentValues();
                        String header = ((EditText) ll.findViewById(R.id.alert_tag_body)).getText().toString();
                        values.put(FeedsTable.TAGS_COLUMN_BODY, header);
                        values.put(FeedsTable.TAGS_COLUMN_TASKID, taskId);
                        getContentResolver().insert(GlobalContext.TAGS_URI, values);
                    }
                });

                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        ListView listTasks = (ListView) findViewById(R.id.details_list_tags);
        tagAdapter = new TagAdapter(this, null, this);
        listTasks.setAdapter(tagAdapter);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, GlobalContext.TAGS_URI, null, FeedsTable.TAGS_COLUMN_TASKID + "=" + taskId, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        tagAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        tagAdapter.swapCursor(null);
    }
}
