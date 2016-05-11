package com.csc.simple_todo_list;

import android.database.Cursor;

import static android.provider.BaseColumns._ID;
import static com.csc.simple_todo_list.ToDoTable.*;

/**
 * Created by anastasia on 08.04.16.
 */
public class Task implements Comparable<Task> {
    int id;
    String title;
    String date;
    String description;
    boolean isStarred;
    boolean isDone;

    public Task(int id, String title,String description, String date, boolean isStarred, boolean isDone) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.isDone = isDone;
        this.isStarred = isStarred;
        this.description = description;
    }
    public int compareTo(Task o) {
        if (this.isDone != o.isDone) {
            return (this.isDone ? 1 : -1);
        } else if (this.isStarred == o.isStarred) {
            return this.date.compareTo(o.date);
        } else {
            return (this.isStarred ? -1 : 1);
        }
    }
    public static Task fromCursor(Cursor cursor) {
        return new Task(
                (int)cursor.getLong(cursor.getColumnIndex(_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_STARRED)) > 0,
                cursor.getInt(cursor.getColumnIndex(COLUMN_DONE)) > 0);
    }
}
