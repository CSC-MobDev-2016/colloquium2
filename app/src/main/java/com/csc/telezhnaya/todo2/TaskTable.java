package com.csc.telezhnaya.todo2;

import android.provider.BaseColumns;

interface TaskTable extends BaseColumns {
    String TABLE_NAME = "todo";

    String COLUMN_TEXT = "task_text";
    String COLUMN_DESCRIPTION = "task_description";
    String COLUMN_DATE = "creation_date";
    String COLUMN_STATUS = "status";
    String COLUMN_STARRED = "starred";
}
