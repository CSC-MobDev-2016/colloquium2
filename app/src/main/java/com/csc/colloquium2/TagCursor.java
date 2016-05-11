package com.csc.colloquium2;

import android.database.Cursor;
import android.database.CursorWindow;
import android.database.CursorWrapper;

/**
 * Created by Nataly on 11.05.2016.
 */
public class TagCursor extends CursorWrapper {
    public TagCursor(Cursor cursor) {
        super(cursor);
    }

    public String getTag() {
        if (isBeforeFirst() || isAfterLast()) {
            return null;
        }
        return getString(getColumnIndex(TagsTable.COLUMN_TEXT));
    }
}
