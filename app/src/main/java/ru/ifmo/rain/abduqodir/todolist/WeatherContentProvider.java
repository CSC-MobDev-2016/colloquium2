package ru.ifmo.rain.abduqodir.todolist;

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

public class WeatherContentProvider extends ContentProvider {
  public static final String AUTHORITY = "ru.ifmo.rain.abduqodir.todolist";
  private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
  public static final Uri TASK_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "task");
  public static final Uri TAG_CONTENT_URI = Uri.withAppendedPath(CONTENT_URI, "tag");

  public static final String TASK_SORT_ORDER = TaskTable.COLUMN_DONE  + " ASC, " +
      TaskTable.COLUMN_STARRED + " DESC, " + TaskTable.COLUMN_DATE + " DESC";

  public static final int TASK_ENTRIES = 1;
  public static final int TAG_ENTRIES = 2;
  public static final int TASK_ENTRIES_ID = 3;
  public static final int TAG_ENTRIES_ID = 4;

  private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    uriMatcher.addURI(AUTHORITY, "/task/entries", TASK_ENTRIES);
    uriMatcher.addURI(AUTHORITY, "/tag/entries", TAG_ENTRIES);
    uriMatcher.addURI(AUTHORITY, "/task/entries/#", TASK_ENTRIES_ID);
    uriMatcher.addURI(AUTHORITY, "/tag/entries/#", TAG_ENTRIES_ID);
  }

  private WeatherOpenHelper helper;

  public WeatherContentProvider() {
    helper = new WeatherOpenHelper(getContext());
  }

  @Override
  public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
    String tableName;
    switch (uriMatcher.match(uri)) {
      case TASK_ENTRIES:
        tableName = TaskTable.TABLE_NAME;
        break;
      case TASK_ENTRIES_ID:
        tableName = TaskTable.TABLE_NAME;
        String taskId = uri.getLastPathSegment();

        selection = (selection != null && !selection.isEmpty() ? selection + " AND " : "")
            + TaskTable._ID + " = " + taskId;
        break;
      case TAG_ENTRIES:
        tableName = TagTable.TABLE_NAME;
        break;
      case TAG_ENTRIES_ID:
        tableName = TagTable.TABLE_NAME;
        String tagId = uri.getLastPathSegment();

        selection = (selection != null && !selection.isEmpty() ? selection + " AND " : "")
            + TagTable._ID + " = " + tagId;
        break;
      default:
        throw new IllegalArgumentException("Wrong URI: " + uri);
    }

    int result = helper.getWritableDatabase().delete(tableName, selection, selectionArgs);
    getContext().getContentResolver().notifyChange(uri, null);
    return result;
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
    switch (match) {
      case TASK_ENTRIES:
        tableName = TaskTable.TABLE_NAME;
        break;
      case TAG_ENTRIES:
        tableName = TagTable.TABLE_NAME;
        break;
      default:
        throw new UnsupportedOperationException("Not yet implemented");
    }
    long rowId = helper.getWritableDatabase().insert(tableName, "?", values);
    Uri inserted = ContentUris.withAppendedId(uri, rowId);
    getContext().getContentResolver().notifyChange(inserted, null);
    return inserted;
  }

  @Override
  public boolean onCreate() {
    helper = new WeatherOpenHelper(getContext());
    return true;
  }

  @Override
  public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String sortOrder) {
    int match = uriMatcher.match(uri);
    String tableName;
    switch (match) {
      case TASK_ENTRIES:
        tableName = TaskTable.TABLE_NAME;
        break;
      case TASK_ENTRIES_ID:
        tableName = TaskTable.TABLE_NAME;
        String taskId = uri.getLastPathSegment();

        selection = (selection != null && !selection.isEmpty() ? selection + " AND " : "")
            + TaskTable._ID + " = " + taskId;
        break;
      case TAG_ENTRIES:
        tableName = TagTable.TABLE_NAME;
        break;
      case TAG_ENTRIES_ID:
        tableName = TagTable.TABLE_NAME;
        String tagId = uri.getLastPathSegment();

        selection = (selection != null && !selection.isEmpty() ? selection + " AND " : "")
            + TagTable._ID + " = " + tagId;
        break;
      default:
        throw new UnsupportedOperationException("Not yet implemented");
    }
    SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
    builder.setTables(tableName);
    SQLiteDatabase db = helper.getReadableDatabase();
    Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }

  @Override
  public int update(@NonNull Uri uri, ContentValues values, String selection,
                    String[] selectionArgs) {

    Log.d("update", values.toString());
    String tableName;
    int match = uriMatcher.match(uri);
    switch (match) {
      case TASK_ENTRIES:
        tableName = TaskTable.TABLE_NAME;
        break;
      case TASK_ENTRIES_ID:
        tableName = TaskTable.TABLE_NAME;
        String taskId = uri.getLastPathSegment();

        selection = (selection != null && !selection.isEmpty() ? selection + " AND " : "")
            + TaskTable._ID + " = " + taskId;
        break;
      case TAG_ENTRIES:
        tableName = TagTable.TABLE_NAME;
        break;
      case TAG_ENTRIES_ID:
        tableName = TagTable.TABLE_NAME;
        String tagId = uri.getLastPathSegment();

        selection = (selection != null && !selection.isEmpty() ? selection + " AND " : "")
            + TagTable._ID + " = " + tagId;
        break;
      default:
        throw new UnsupportedOperationException("Not yet implemented");
    }
    int result = helper.getWritableDatabase().update(tableName, values, selection, selectionArgs);
    getContext().getContentResolver().notifyChange(uri, null);
    return result;
  }
}
