package com.csc.telezhnaya.todo2;

import android.provider.BaseColumns;

interface TaskTable extends BaseColumns {
    String TABLE_NAME = "todo";

    String COLUMN_TEXT = "task_text";
    String COLUMN_DATE = "date";
    String COLUMN_STATUS = "status";
    String COLUMN_STARRED = "starred";
}
