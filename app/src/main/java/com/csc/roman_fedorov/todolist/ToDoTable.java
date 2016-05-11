package com.csc.roman_fedorov.todolist;

import android.provider.BaseColumns;

interface ToDoTable extends BaseColumns {
    String TABLE_NAME = "ToDoTasks";

    String COLUMN_TITLE = "title";
    String COLUMN_DESCRIPTION = "description";
    String COLUMN_FAVOURITE = "favourite";
}
