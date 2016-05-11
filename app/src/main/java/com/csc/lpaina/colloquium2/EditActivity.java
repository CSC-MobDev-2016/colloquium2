package com.csc.lpaina.colloquium2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity implements
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TAG_ID = 0;
    private static final String TAG = "EditActivity";
    private final int TAGS_LOADER = 2;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private CheckBox checkBox;
    private RatingBar ratingBar;
    private int columnId;
    private LinearLayout tagsLayout;

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

        getTags();

        Button editFinished = (Button) findViewById(R.id.button_ok);
        editFinished.setOnClickListener(this);

        Button addTag = (Button) findViewById(R.id.add_tag);
        addTag.setOnClickListener(this);

        tagsLayout = (LinearLayout) findViewById(R.id.ll_tags);
    }

    private void getTags() {
        getSupportLoaderManager().initLoader(TAGS_LOADER, null, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_ok) {
            ContentValues values = new ContentValues();
            values.put(TodoTable.COLUMN_TITLE, textViewTitle.getText().toString());
            values.put(TodoTable.COLUMN_DESCRIPTION, textViewDescription.getText().toString());
            values.put(TodoTable.COLUMN_RANGE, (int) ratingBar.getRating());
            if (checkBox.isChecked()) {
                values.put(TodoTable.COLUMN_STATUS, 1);
            } else {
                values.put(TodoTable.COLUMN_STATUS, -1);
            }

            getContentResolver().update(
                    ContentUris.withAppendedId(MainActivity.ENTRIES_URI, columnId), values,
                    TodoTable._ID + "=?", new String[]{String.valueOf(columnId)});
            finish();
            return;
        }
        if (v.getId() == R.id.add_tag) {
            EditText et_tag = (EditText) findViewById(R.id.et_tag);
            final String tag = et_tag.getText().toString();

            //add to table
            ContentValues values = new ContentValues();
            values.put(TagTable.COLUMN_TAG, tag);
            values.put(TagTable.COLUMN_TASK_ID, columnId);

            Uri uri = getContentResolver().insert(MainActivity.TAGS_URI, values);

            Integer tag_id = Integer.getInteger(uri.getLastPathSegment());

            //add to view

            final TextView textView = new TextView(this);
            textView.setText(tag);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Integer id = (Integer) v.getTag(TAG_ID);
                    getContentResolver().delete(
                            MainActivity.TAGS_URI,
                            TagTable._ID + "=?", new String[]{String.valueOf(id)});
                    tagsLayout.removeView(v);
                    return true;
                }
            });
            textView.setTag(TAG_ID, tag_id);
            tagsLayout.addView(textView, 0);

            return;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: id= " + id);
        switch (id) {
            case TAGS_LOADER:
                return new CursorLoader(this, MainActivity.TAGS_URI, null, null, null, null);
            default:
                throw new IllegalArgumentException("Argument id = " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // наши данные загрузились, вот тебе курсор
        int id = loader.getId();
        Log.d(TAG, "onLoadFinished: id = " + id);
        switch (id) {
            case TAGS_LOADER:
                while (cursor.moveToNext()) {
                    int task_id = cursor.getInt(cursor.getColumnIndex(TagTable.COLUMN_TASK_ID));
                    String tag = cursor.getString(cursor.getColumnIndex(TagTable.COLUMN_TAG));
                    int tag_id = cursor.getInt(cursor.getColumnIndex(TagTable._ID));
                    if (task_id == columnId) {
                        final TextView textView = new TextView(this);
                        textView.setText(tag);
                        textView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Integer id = (Integer) v.getTag(TAG_ID);
                                getContentResolver().delete(
                                        MainActivity.TAGS_URI,
                                        TagTable._ID + "=?", new String[]{String.valueOf(id)});
                                tagsLayout.removeView(v);
                                return true;
                            }
                        });
                        textView.setTag(TAG_ID, tag_id);
                        tagsLayout.addView(textView, 0);
                    }
                    cursor.close();
                }
                Log.d(TAG, "onLoadFinished: items changed");
                break;
            default:
                throw new IllegalArgumentException("UnknownLoader id = " + id);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // наши данные закрылись, не надо этот курсор держать
        int id = loader.getId();
        Log.d(TAG, "onLoaderReset: id = " + id);
        switch (id) {
            case TAGS_LOADER:

                break;
            default:
                throw new IllegalArgumentException("Argument id = " + id);
        }
    }
}
