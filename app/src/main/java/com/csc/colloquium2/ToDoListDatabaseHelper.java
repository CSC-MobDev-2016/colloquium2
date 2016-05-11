package com.csc.colloquium2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ToDoListDatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = "ToDoListDatabaseHelper";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "todolist.db";

    private static final String SQL_CREATE_TASK_TABLE =
            "CREATE TABLE " + TasksTable.TABLE_NAME
                    + " ("
                    + TasksTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TasksTable.COLUMN_TEXT + " TEXT, "
                    + TasksTable.COLUMN_TITLE + " TEXT, "
                    + TasksTable.COLUMN_FAVORITE + " BOOLEAN, "
                    + TasksTable.COLUMN_COMPLETED + " BOOLEAN, "
                    + TasksTable.COLUMN_DATE + " DATETIME"
                    + ")";

    private static final String SQL_CREATE_TAGS_TABLE =
            "CREATE TABLE " + TagsTable.TABLE_NAME
                    + " ("
                    + TagsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TagsTable.COLUMN_TEXT + " TEXT, "
                    + TagsTable.COLUMN_TASK_ID + " INTEGER"
                    + ")";

    public ToDoListDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASK_TABLE);
        Log.d(TAG, "Task table created.");

        db.execSQL(SQL_CREATE_TAGS_TABLE);
        Log.d(TAG, "TAGS tables created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
