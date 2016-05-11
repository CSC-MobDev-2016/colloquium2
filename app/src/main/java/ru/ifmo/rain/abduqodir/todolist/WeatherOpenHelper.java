package ru.ifmo.rain.abduqodir.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherOpenHelper extends SQLiteOpenHelper {
  public static final int DATABASE_VERSION = 4;
  public static final String DATABASE_NAME = "tasks.db";

  private static final String SQL_CREATE_TASK_ENTRIES_TABLE =
      "CREATE TABLE " + TaskTable.TABLE_NAME
          + "("
          + TaskTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
          + TaskTable.COLUMN_TITLE + " TEXT, "
          + TaskTable.COLUMN_CONTENT + " TEXT, "
          + TaskTable.COLUMN_DATE + " TEXT, "
          + TaskTable.COLUMN_DONE + " TEXT, "
          + TaskTable.COLUMN_STARRED + " TEXT"
          + ")";

  private static final String SQL_CREATE_TAG_ENTRIES_TABLE =
      "CREATE TABLE " + TagTable.TABLE_NAME
          + "("
          + TagTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
          + TagTable.COLUMN_TITLE + " TEXT"
          + ")";

  private static final String SQL_DELETE_TASK_ENTRIES = "DROP TABLE IF EXISTS " + TaskTable.TABLE_NAME;
  private static final String SQL_DELETE_TAG_ENTRIES = "DROP TABLE IF EXISTS " + TagTable.TABLE_NAME;

  public WeatherOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_TASK_ENTRIES_TABLE);
    db.execSQL(SQL_CREATE_TAG_ENTRIES_TABLE);
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(SQL_DELETE_TASK_ENTRIES);
    db.execSQL(SQL_DELETE_TAG_ENTRIES);
    onCreate(db);
  }
}
