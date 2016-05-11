package com.csc.simple_todo_list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "todo.db";

    private static final String SQL_CREATE_ENTRIES_TABLE =
            "CREATE TABLE " + ToDoTable.TABLE_NAME
                    + "("
                    + ToDoTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ToDoTable.COLUMN_TITLE + " TEXT, "
                    + ToDoTable.COLUMN_DESCRIPTION + " TEXT, "
                    + ToDoTable.COLUMN_DATE + " TEXT, "
                    + ToDoTable.COLUMN_STARRED + " INT, "
                    + ToDoTable.COLUMN_DONE + " INT"
                    + ")";
    private static final String SQL_CREATE_TAG_TABLE =
            "CREATE TABLE " + TagTable.TABLE_NAME
                    + "("
                    + TagTable.COLUMN_TASK_ID + " INT, "
                    + TagTable.COLUMN_TAG + " TEXT "
                    + ")";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ToDoTable.TABLE_NAME;
    private static final String SQL_DELETE_TAGS = "DROP TABLE IF EXISTS " + TagTable.TABLE_NAME;

    public ToDoOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_TABLE);
        db.execSQL(SQL_CREATE_TAG_TABLE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_TAGS);
        onCreate(db);
    }
}
