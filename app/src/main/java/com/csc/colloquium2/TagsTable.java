package com.csc.colloquium2;

import android.provider.BaseColumns;

/**
 * Created by Nataly on 11.05.2016.
 */
public interface TagsTable extends BaseColumns {
    String TABLE_NAME = "tags";

    String COLUMN_TASK_ID = "tags";
    String COLUMN_TEXT = "text";
}
