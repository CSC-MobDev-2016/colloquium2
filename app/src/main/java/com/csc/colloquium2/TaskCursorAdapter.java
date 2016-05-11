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

public class TaskCursorAdapter extends CursorAdapter {
    public static final String TAG = "TaskCursorAdapter";

    private Context context;
    private TaskCursor taskCursor;

    public TaskCursorAdapter(Context context, TaskCursor c) {
        super(context, c, 0);
        this.taskCursor = c;
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Task task = taskCursor.getTask();

        TextView date = (TextView) view.findViewById(R.id.date);
        TextView title = (TextView) view.findViewById(R.id.task_title);
        ImageView fav = (ImageView) view.findViewById(R.id.favorite_img);

        date.setText(task.getDate());
        title.setText(task.getTitle());
        fav.setVisibility(
                task.isFavorite() ? View.VISIBLE : View.INVISIBLE
        );
        if (task.isCompleted()) {

            Log.d(TAG, "task is completed - " + task.getId());

            title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            title.setPaintFlags(title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        taskCursor = (TaskCursor) newCursor;
        return super.swapCursor(newCursor);
    }
}
