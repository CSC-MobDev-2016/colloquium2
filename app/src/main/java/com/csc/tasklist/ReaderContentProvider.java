package com.csc.tasklist;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

public class ReaderContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.csc.tasklist";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final int ENTRIES = 1;
    public static final int TAGS = 3;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        uriMatcher.addURI(AUTHORITY, "/entries", ENTRIES);
        uriMatcher.addURI(AUTHORITY, "/tags", TAGS);
    }

    private ReaderOpenHelper helper;
    private TagsOpenHelper helperTags;

    public ReaderContentProvider() {
        helper = new ReaderOpenHelper(getContext());
        helperTags = new TagsOpenHelper(getContext());
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        String tableName;
        int count = 0;
        switch (match) {
            case ENTRIES:
                tableName = FeedsTable.TABLE_NAME;
                count = helper.getWritableDatabase().delete(tableName, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case TAGS:
                tableName = FeedsTable.TAGS_TABLE;
                count = helperTags.getWritableDatabase().delete(tableName, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int match = uriMatcher.match(uri);
        String tableName;
        Uri inserted = null;
        long rowId = 0;
        switch (match) {
            case ENTRIES:
                tableName = FeedsTable.TABLE_NAME;
                rowId = helper.getWritableDatabase().insert(tableName, null, values);
                inserted = ContentUris.withAppendedId(uri, rowId);
                break;
            case TAGS:
                tableName = FeedsTable.TAGS_TABLE;
                rowId = helperTags.getWritableDatabase().insert(tableName, null, values);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        inserted = ContentUris.withAppendedId(uri, rowId);
        getContext().getContentResolver().notifyChange(inserted, null);
        return inserted;
    }

    @Override
    public boolean onCreate() {
        helper = new ReaderOpenHelper(getContext());
        helperTags = new TagsOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int match = uriMatcher.match(uri);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        SQLiteDatabase db;
        switch (match) {
            case ENTRIES:
                builder.setTables(FeedsTable.TABLE_NAME);
                db = helper.getReadableDatabase();
                break;
            case TAGS:
                builder.setTables(FeedsTable.TAGS_TABLE);
                db = helperTags.getReadableDatabase();
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }

        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        String tableName;
        int count = 0;
        switch (match) {
            case ENTRIES:
                tableName = FeedsTable.TABLE_NAME;
                count = helper.getWritableDatabase().update(tableName, values, selection, selectionArgs);
                break;
            case TAGS:
                tableName = FeedsTable.TAGS_TABLE;
                count = helperTags.getWritableDatabase().update(tableName, values, selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
