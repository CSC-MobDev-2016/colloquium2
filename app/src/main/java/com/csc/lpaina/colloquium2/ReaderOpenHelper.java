package com.csc.lpaina.colloquium2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class ReaderOpenHelper extends SQLiteOpenHelper {
    public static final String SQL_CREATE_ENTRIES_TABLE =
            "CREATE TABLE " + TodoTable.TABLE_NAME
                    + "("
                    + TodoTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TodoTable.COLUMN_TITLE + " TEXT, "
                    + TodoTable.COLUMN_DESCRIPTION + " TEXT, "
                    + TodoTable.COLUMN_RANGE + " INTEGER, "
                    + TodoTable.COLUMN_STATUS + " INTEGER"
                    + ")";
    public static final String SQL_CREATE_TAGS_TABLE =
            "CREATE TABLE " + TagTable.TABLE_NAME
                    + "("
                    + TagTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TagTable.COLUMN_TAG + " TEXT, "
                    + TagTable.COLUMN_TASK_ID + " INTEGER "
                    + ")";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo.db";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TodoTable.TABLE_NAME + "; "
            + "DROP TABLE IF EXISTS " + TagTable.TABLE_NAME;

    public ReaderOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_TABLE);
        db.execSQL(SQL_CREATE_TAGS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
