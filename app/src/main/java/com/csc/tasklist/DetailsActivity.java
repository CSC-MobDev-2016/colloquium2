package com.csc.tasklist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Oleg Doronin
 * colloquium2
 * Copyright (c) 2016 CS. All rights reserved.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        String taskId = getIntent().getStringExtra(GlobalContext.TASK_ID);
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(MainActivity.ENTRIES_URI, null, FeedsTable._ID + "=" + taskId, null, null);
            if (cursor.moveToNext()) {
                String header = cursor.getString(cursor.getColumnIndex(FeedsTable.COLUMN_HEADER));
                String body = cursor.getString(cursor.getColumnIndex(FeedsTable.COLUMN_BODY));
                TextView tvHeader = (TextView)findViewById(R.id.details_header);
                TextView tvBody = (TextView)findViewById(R.id.details_body);
                tvHeader.setText(header);
                tvBody.setText(body);
            }
        } catch (Exception ex) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
