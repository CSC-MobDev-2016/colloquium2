package com.csc.lpaina.colloquium2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class ReaderOpenHelper extends SQLiteOpenHelper {
    public static final String SQL_CREATE_ENTRIES_TABLE =
            "CREATE TABLE " + FeedsTable.TABLE_NAME
                    + "("
                    + FeedsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FeedsTable.COLUMN_TITLE + " TEXT, "
                    + FeedsTable.COLUMN_DESCRIPTION + " TEXT, "
                    + FeedsTable.COLUMN_RANGE + " INTEGER, "
                    + FeedsTable.COLUMN_STATUS + " INTEGER"
                    + ")";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo.db";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedsTable.TABLE_NAME;

    public ReaderOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_TABLE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
