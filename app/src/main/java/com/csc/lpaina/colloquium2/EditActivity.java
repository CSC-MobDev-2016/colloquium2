package com.csc.lpaina.colloquium2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewTitle;
    private TextView textViewDescription;
    private CheckBox checkBox;
    private RatingBar ratingBar;
    private int columnId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        textViewTitle = (TextView) findViewById(R.id.edit_title);
        textViewDescription = (TextView) findViewById(R.id.edit_description);
        checkBox = (CheckBox) findViewById(R.id.check_box_edit);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        columnId = intent.getIntExtra(RVAdapter.CardViewHolder.COLUMN_ID, 0);

        textViewTitle.setText(intent.getStringExtra(RVAdapter.CardViewHolder.TITLE));
        textViewDescription.setText(intent.getStringExtra(RVAdapter.CardViewHolder.DESCRIPTION));
        checkBox.setChecked(intent.getBooleanExtra(RVAdapter.CardViewHolder.CHECKED, false));
        ratingBar.setRating(intent.getIntExtra(RVAdapter.CardViewHolder.RANGE, 0));

        Button editFinished = (Button) findViewById(R.id.button_ok);
        editFinished.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ContentValues values = new ContentValues();
        values.put(FeedsTable.COLUMN_TITLE, textViewTitle.getText().toString());
        values.put(FeedsTable.COLUMN_DESCRIPTION, textViewDescription.getText().toString());
        values.put(FeedsTable.COLUMN_RANGE, (int) ratingBar.getRating());
        if (checkBox.isChecked()) {
            values.put(FeedsTable.COLUMN_STATUS, 1);
        } else {
            values.put(FeedsTable.COLUMN_STATUS, -1);
        }

        getContentResolver().update(ContentUris.withAppendedId(MainActivity.ENTRIES_URI, columnId), values,
                FeedsTable._ID + "=?", new String[]{String.valueOf(columnId)});
        finish();
    }

}
