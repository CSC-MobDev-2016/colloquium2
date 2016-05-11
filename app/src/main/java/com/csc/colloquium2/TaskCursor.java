package com.csc.colloquium2;

import android.database.Cursor;
import android.database.CursorWrapper;

public class TaskCursor extends CursorWrapper {
    public TaskCursor(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        if (isBeforeFirst() || isAfterLast()) {
            return null;
        }
        Task task = new Task();
        task.setId(getLong(getColumnIndex(TasksTable._ID)));
        task.setText(getString(getColumnIndex(TasksTable.COLUMN_TEXT)));
        task.setTitle(getString(getColumnIndex(TasksTable.COLUMN_TITLE)));
        task.setDate(getString(getColumnIndex(TasksTable.COLUMN_DATE)));
        task.setCompleted(Boolean.parseBoolean(
                getString(getColumnIndex(TasksTable.COLUMN_COMPLETED))
        ));
        task.setFavorite(Boolean.parseBoolean(
                getString(getColumnIndex(TasksTable.COLUMN_FAVORITE))
        ));
        return task;
    }
}
