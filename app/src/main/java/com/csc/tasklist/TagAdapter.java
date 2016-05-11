package com.csc.tasklist;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Oleg Doronin
 * colloquium2
 * Copyright (c) 2016 CS. All rights reserved.
 */
public class TagAdapter extends CursorAdapter {

    private DetailsActivity activity;
    private int paintFlags;

    TagAdapter(Context context, Cursor cursor, DetailsActivity activity) {
        super(context, cursor, 0);
        this.activity = activity;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_tag, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final LinearLayout llItemTask = (LinearLayout) view.findViewById(R.id.item_tag);
        final TextView tvBody = (TextView) view.findViewById(R.id.item_tag_body);

        final int id = cursor.getInt(cursor.getColumnIndex(FeedsTable._ID));
        final String body = cursor.getString(cursor.getColumnIndex(FeedsTable.TAGS_COLUMN_BODY));
        tvBody.setText(body);
        llItemTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.remove));
                final LinearLayout ll = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.tag_delete, null);
                builder.setView(ll);
                ((TextView) ll.findViewById(R.id.alert_tag_delete)).setText(body);

                builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.getContentResolver().delete(GlobalContext.TAGS_URI, FeedsTable._ID + "=" + Integer.toString(id), null);
                    }
                });
                builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


                builder.show();
                return true;
            }
        });
    }
}
