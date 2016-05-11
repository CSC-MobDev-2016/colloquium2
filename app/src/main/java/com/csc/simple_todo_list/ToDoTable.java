package com.csc.simple_todo_list;

import android.provider.BaseColumns;

interface ToDoTable extends BaseColumns {
    String TABLE_NAME = "todo";

    String COLUMN_TITLE = "title";
    String COLUMN_DESCRIPTION = "description";
    String COLUMN_DATE = "date";
    String COLUMN_STARRED = "starred";
    String COLUMN_DONE = "done";
}
