package com.csc.lpaina.colloquium2;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    public static final Uri ENTRIES_URI = Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "entries");
    private final int ENTRIES_LOADER = 1;

    final private RVAdapter adapter = new RVAdapter(null);
    private EditText editText;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        editText = (EditText) findViewById(R.id.edit_text);
        Button button = (Button) findViewById(R.id.button_add);
        button.setOnClickListener(this);
        button.requestFocus();

        Cursor cursor = getContentResolver().query(ENTRIES_URI, null, null, null, null);
        adapter.updateCursor(cursor);
        recyclerView.setAdapter(adapter);
        layoutManager.onItemsChanged(recyclerView);
    }

    public void addData(String title, String description) {
        ContentValues values = new ContentValues();
        values.put(FeedsTable.COLUMN_TITLE, title);
        values.put(FeedsTable.COLUMN_DESCRIPTION, description);
        values.put(FeedsTable.COLUMN_RANGE, -1);
        values.put(FeedsTable.COLUMN_STATUS, false);

        getContentResolver().insert(ENTRIES_URI, values);
    }

    @Override
    public void onClick(View v) {
        addData(editText.getText().toString(), "");
        editText.setText("");
        editText.clearFocus();
        layoutManager.onItemsChanged(recyclerView);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ENTRIES_LOADER:
                return new CursorLoader(this, ENTRIES_URI, null, null, null, null);
            default:
                throw new IllegalArgumentException("Argument id = " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // наши данные загрузились, вот тебе курсор
        int id = loader.getId();
        switch (id) {
            case ENTRIES_LOADER:
                adapter.updateCursor(data);
                layoutManager.onItemsChanged(recyclerView);
                break;
            default:
                throw new IllegalArgumentException("UnknownLoader id = " + id);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // наши данные закрылись, не надо этот курсор держать
        int id = loader.getId();
        switch (id) {
            case ENTRIES_LOADER:
                adapter.updateCursor(null);
                break;
            default:
                throw new IllegalArgumentException("Argument id = " + id);
        }
    }

}
