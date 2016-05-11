package com.csc.simple_todo_list;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.*;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;


public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder>{
    private MainActivity activity;
    private Context context;

    public MyListCursorAdapter(Context context,Cursor cursor){
        super(context,cursor);
        activity = (MainActivity) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        context = parent.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        Task task = Task.fromCursor(cursor);

        viewHolder.titleView.setText(task.title);
        viewHolder.done.setChecked(task.isDone);
        if (task.isDone)
        {
            SpannableString content = new SpannableString(task.title);
            content.setSpan(new StrikethroughSpan(), 0, task.title.length(), 0);
            viewHolder.titleView.setText(content);
        }
        viewHolder.titleView.setTag(task.id);
        viewHolder.dateView.setText(task.date);
        viewHolder.isStarred = task.isStarred;
        if (task.isStarred) {
            viewHolder.star.setImageResource(R.drawable.star);
        } else {
            viewHolder.star.setImageResource(R.drawable.emptystar);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, View.OnClickListener {
        TextView titleView;
        TextView dateView;
        ImageView star;
        CheckBox done;
        boolean isStarred;

        ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView)itemView.findViewById(R.id.titleView);
            dateView = (TextView)itemView.findViewById(R.id.dateView);
            star = (ImageView)itemView.findViewById(R.id.star);
            done = (CheckBox)itemView.findViewById(R.id.done);
            star.setOnClickListener(this);
            done.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v instanceof ImageView) {
                isStarred = !isStarred;
                activity.updateTask((int) titleView.getTag(), titleView.getText().toString(), dateView.getText().toString(),
                        done.isChecked(), isStarred);
            } else if (v instanceof CheckBox) {
                activity.updateTask((int) titleView.getTag(), titleView.getText().toString(), dateView.getText().toString(),
                        done.isChecked(), isStarred);
            }
            else {
                activity.showData(titleView.getText().toString());
            }


        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Rename");
            menu.add(0, v.getId(), 0, "Delete");
            menu.getItem(0).setOnMenuItemClickListener(onFileRename);
            menu.getItem(1).setOnMenuItemClickListener(onFileDelete);
        }



        private final MenuItem.OnMenuItemClickListener onFileRename = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final EditText editText = new EditText(context);
                editText.setText(titleView.getText().toString());
                AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                        .setMessage("Enter new task")
                        .setView(editText)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                titleView.setText(editText.getText().toString());
                                ViewHolder.this.onClick(titleView);
                            }})
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {}});
                builder.create().show();
                return true;
            }
        };

        private final MenuItem.OnMenuItemClickListener onFileDelete = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                        .setMessage("Delete ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }})
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {}});
                builder.create().show();
                return true;
            }
        };
    }
}
