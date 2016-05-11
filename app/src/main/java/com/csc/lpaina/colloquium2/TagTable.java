package com.csc.lpaina.colloquium2;

import android.provider.BaseColumns;

interface TagTable extends BaseColumns {
    String TABLE_NAME = "tag";
    String COLUMN_TAG = "tagname";
    String COLUMN_TASK_ID = "idtask";
}
