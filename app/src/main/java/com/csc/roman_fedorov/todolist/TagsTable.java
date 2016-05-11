package com.csc.roman_fedorov.todolist;

import android.provider.BaseColumns;

/**
 * Created by roman on 11.05.2016.
 */
public interface TagsTable extends BaseColumns {
    String TABLE_NAME = "Tags";

    String COLUMN_TASK_ID = "taskId";
    String COLUMN_TAG = "tag";
}
