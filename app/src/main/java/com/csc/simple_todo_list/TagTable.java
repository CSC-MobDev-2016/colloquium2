package com.csc.simple_todo_list;

import android.provider.BaseColumns;

/**
 * Created by anastasia on 11.05.16.
 */
interface TagTable extends BaseColumns {
    String TABLE_NAME = "tag";

    String COLUMN_TASK_ID = "task_id";
    String COLUMN_TAG = "tag";
}