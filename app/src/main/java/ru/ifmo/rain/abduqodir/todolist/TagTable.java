package ru.ifmo.rain.abduqodir.todolist;

import android.provider.BaseColumns;

/**
 * Created by abduqodir on 11.05.16.
 */
public interface TagTable extends BaseColumns{
  String TABLE_NAME = "tags";

  String COLUMN_TITLE = "title";
}
