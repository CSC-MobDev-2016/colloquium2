package com.csc.lpaina.colloquium2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;


public class ReaderContentProvider extends ContentProvider {
    private static final String AUTHORITY = "com.csc.lpaina.colloquium2";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final int ENTRIES = 1;
    private static final int ENTRIES_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final String TAG = "ContentProvider";

    static {
        uriMatcher.addURI(AUTHORITY, "/entries", ENTRIES);
        uriMatcher.addURI(AUTHORITY, "/entries/#", ENTRIES_ID);
    }

    private ReaderOpenHelper helper;

    public ReaderContentProvider() {
        helper = new ReaderOpenHelper(getContext());
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = uriMatcher.match(uri);
        String tableName;
        Log.d(TAG, "insert: match = " + match);
        switch (match) {
            case ENTRIES:
                tableName = TodoTable.TABLE_NAME;
                break;
            case ENTRIES_ID:
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        long rowId = helper.getWritableDatabase().insert(tableName, null, values);
        Uri inserted = ContentUris.withAppendedId(uri, rowId);
        getContext().getContentResolver().notifyChange(inserted, null);
        return inserted;
    }

    @Override
    public boolean onCreate() {
        helper = new ReaderOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int match = uriMatcher.match(uri);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (match) {
            case ENTRIES:
                builder.setTables(TodoTable.TABLE_NAME);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = TodoTable.COLUMN_STATUS + ", " + TodoTable.COLUMN_RANGE + " ASC";
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {

            case ENTRIES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = TodoTable._ID + " = " + id;
                } else {
                    selection = selection + " AND " + TodoTable._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        int cnt = db.update(TodoTable.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

}
