package com.csc.roman_fedorov.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "todoDatabase";

    private static TodoDatabaseHelper sInstance;

    public static synchronized TodoDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TodoDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_TAGS_TABLE =
            "CREATE TABLE " + TagsTable.TABLE_NAME
                    + "("
                    + TagsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TagsTable.COLUMN_TASK_ID + " INTEGER, "
                    + TagsTable.COLUMN_TAG + " TEXT"
                    + ")";


    private static final String SQL_CREATE_TODOS_TABLE =
            "CREATE TABLE " + ToDoTable.TABLE_NAME
                    + "("
                    + ToDoTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ToDoTable.COLUMN_TITLE + " TEXT, "
                    + ToDoTable.COLUMN_DESCRIPTION + " TEXT, "
                    + ToDoTable.COLUMN_FAVOURITE + " TEXT"
                    + ")";

    private static final String SQL_DELETE_TODOS = "DROP TABLE IF EXISTS " + ToDoTable.TABLE_NAME;
    private static final String SQL_DELETE_TAGS = "DROP TABLE IF EXISTS " + TagsTable.TABLE_NAME;

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TODOS_TABLE);
        db.execSQL(SQL_CREATE_TAGS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            db.execSQL(SQL_DELETE_TODOS);
            db.execSQL(SQL_DELETE_TAGS);
            onCreate(db);
        }
    }
}
