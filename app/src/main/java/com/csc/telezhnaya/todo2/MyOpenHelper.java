package com.csc.telezhnaya.todo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "com.csc.telezhnaya.todo2";

    private static final String SQL_CREATE_TODO_TABLE =
            "CREATE TABLE " + TaskTable.TABLE_NAME + "("
                    + TaskTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TaskTable.COLUMN_TEXT + " TEXT, "
                    + TaskTable.COLUMN_DESCRIPTION + " TEXT, "
                    + TaskTable.COLUMN_DATE + " INTEGER, "
                    + TaskTable.COLUMN_STARRED + " INTEGER, "
                    + TaskTable.COLUMN_STATUS + " INTEGER)";

    private static final String SQL_CREATE_TAG_TABLE =
            "CREATE TABLE " + TagTable.TABLE_NAME + "("
                    + TagTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TagTable.COLUMN_TASK + " INTEGER, "
                    + TagTable.COLUMN_DESCRIPTION + " TEXT)";

    private static final String SQL_DELETE_TODO_TABLE = "DROP TABLE IF EXISTS " + TaskTable.TABLE_NAME;

    private static final String SQL_DELETE_TAG_TABLE = "DROP TABLE IF EXISTS " + TagTable.TABLE_NAME;

    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TODO_TABLE);
        db.execSQL(SQL_CREATE_TAG_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TODO_TABLE);
        db.execSQL(SQL_DELETE_TAG_TABLE);
        onCreate(db);
    }
}
