package com.csc.telezhnaya.todo2;

import android.provider.BaseColumns;

interface TagTable extends BaseColumns {
    String TABLE_NAME = "todo_tag";

    String COLUMN_TASK = "task_id";
    String COLUMN_DESCRIPTION = "tag_description";
}
