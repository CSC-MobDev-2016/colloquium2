package ru.ifmo.rain.abduqodir.todolist;

import android.provider.BaseColumns;

interface TaskTable extends BaseColumns {
  String TABLE_NAME = "tasks";

  String COLUMN_TITLE = "title";
  String COLUMN_CONTENT = "content";
  String COLUMN_DATE = "date";
  String COLUMN_STARRED = "starred";
  String COLUMN_DONE = "done";
}
