package com.csc.colloquium2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class ToDoListContentProvider extends ContentProvider {
    public static final String TAG = "ToDoListContentProvider";

    public static final String AUTHORITY = "ru.ppzh.todolist.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final int TASKS = 1;
    public static final int TASKS_ID = 2;
    public static final int TAGS = 3;
    public static final int TAGS_ID = 4;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, "/tasks", TASKS);
        uriMatcher.addURI(AUTHORITY, "/tasks/#", TASKS_ID);
        uriMatcher.addURI(AUTHORITY, "/tags", TAGS);
        uriMatcher.addURI(AUTHORITY, "/tags/#", TAGS_ID);
    }

    private ToDoListDatabaseHelper helper;

    public ToDoListContentProvider() {
        helper = new ToDoListDatabaseHelper(getContext());
    }


    @Override
    public boolean onCreate() {
        helper = new ToDoListDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        Log.d(TAG, "Insertion: " + values.toString());

        int match = uriMatcher.match(uri);
        String tableName;
        switch (match) {
            case TASKS:
                tableName = TasksTable.TABLE_NAME;
                break;
            case TAGS:
                tableName = TagsTable.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        long rowId = helper.getWritableDatabase().insert(tableName, null, values);
        Uri inserted = ContentUris.withAppendedId(uri, rowId);
        getContext().getContentResolver().notifyChange(inserted, null);
        return inserted;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Log.d(TAG, "Query");

        int match = uriMatcher.match(uri);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (match) {
            case TASKS:
                builder.setTables(TasksTable.TABLE_NAME);
                break;
            case TAGS:
                builder.setTables(TagsTable.TABLE_NAME);
                break;

            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Log.i(TAG, "Delete");

        int match = uriMatcher.match(uri);
        String tableName;
        switch (match) {
            case TASKS_ID:
                tableName = TasksTable.TABLE_NAME;
                break;
            case TAGS_ID:
                tableName = TasksTable.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        if (selection == null) {
            selection = "";
        }
        selection = selection + "_ID = " + uri.getLastPathSegment();
        int count = helper.getWritableDatabase().delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        Log.d(TAG, "Updating: " + values.toString());

        int match = uriMatcher.match(uri);
        switch (match) {
            case TASKS_ID:
                if (selection == null) {
                    selection = "";
                }
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        int affected =
                helper.getWritableDatabase().update(TasksTable.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
