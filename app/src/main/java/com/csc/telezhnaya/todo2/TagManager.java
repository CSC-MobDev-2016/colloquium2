package com.csc.telezhnaya.todo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.csc.telezhnaya.todo2.MyContentProvider.TAGS_URI;
import static com.csc.telezhnaya.todo2.TagTable.COLUMN_DESCRIPTION;
import static com.csc.telezhnaya.todo2.TagTable.COLUMN_TASK;

public class TagManager implements LoaderManager.LoaderCallbacks<Cursor> {
    private CursorAdapter adapter;
    int position;

    public TagManager(int pos) {
        position = pos;
    }

    private Context context;

    public void bind(Context context) {
        this.context = context;
        adapter = new CursorAdapter(context, null, true) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.tag, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tagName = (TextView) view.findViewById(R.id.tag_name);

                tagName.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                final int id = cursor.getInt(cursor.getColumnIndexOrThrow(TagTable._ID));
                view.setTag(id);

                Button delete = (Button) view.findViewById(R.id.delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteTag(id);
                    }
                });
            }
        };
    }

    public CursorAdapter getAdapter() {
        return adapter;
    }

    public void addTag(long parentId, String text) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, text);
        values.put(COLUMN_TASK, parentId);
        context.getContentResolver().insert(TAGS_URI, values);
    }

    public void deleteTag(long id) {
        context.getContentResolver().delete(TAGS_URI, "_ID=" + id, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(context, TAGS_URI, null, "task_id=" + position, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }
}
