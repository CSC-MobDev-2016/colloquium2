package com.csc.tasklist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Oleg Doronin
 * colloquium2
 * Copyright (c) 2016 CS. All rights reserved.
 */
public class TagsOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tagslist.db";

    private static final String SQL_CREATE_ENTRIES_TABLE =
            "CREATE TABLE " + FeedsTable.TAGS_TABLE
                    + "("
                    + FeedsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FeedsTable.TAGS_COLUMN_TASKID + " INTEGER, "
                    + FeedsTable.TAGS_COLUMN_BODY + " TEXT "
                    + ")";

    public TagsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //skip now
    }
}
