package com.csc.colloquium2;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Nataly on 11.05.2016.
 */
public class TagCursorAdapter extends CursorAdapter {
    private Context context;
    private TagCursor tagCursor;

    public TagCursorAdapter(Context context, TagCursor c) {
        super(context, c, 0);
        this.tagCursor = c;
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.tag_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String tag = tagCursor.getTag();

        TextView title = (TextView) view.findViewById(R.id.textView);
        title.setText(tag);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        tagCursor = (TagCursor) newCursor;
        return super.swapCursor(newCursor);
    }
}
