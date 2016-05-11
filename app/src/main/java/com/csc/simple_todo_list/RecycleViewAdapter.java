package com.csc.simple_todo_list;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by anastasia on 26.03.16.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.FileViewHolder> {
    private List<Task> tasks;
    private MainActivity activity;
    private Context context;

    RecycleViewAdapter(List<Task> tasks, MainActivity activity){
        this.tasks = tasks;
        this.activity = activity;
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_layout, viewGroup, false);
        context = viewGroup.getContext();
        return new FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FileViewHolder personViewHolder, int i) {
        Task task = tasks.get(i);
        //Date lastModDate = new Date(f.lastModified());
        //String dateModify = DateFormat.getDateTimeInstance().format(lastModDate);

        personViewHolder.titleView.setText(task.title);
        //personViewHolder.contentView.setText(task.content);

        //personViewHolder.dateView.setText(dateModify);
        if (task.isStarred) {
            personViewHolder.star.setImageResource(R.drawable.star);
        } else {
            personViewHolder.star.setImageResource(R.drawable.emptystar);
        }
    }

    void setItems(List<Task> list) {
        tasks = list;
        notifyDataSetChanged();
    }

    Task getItem(int pos) {
        return tasks.get(pos);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView titleView;
        TextView contentView;
        TextView dateView;
        ImageView star;
        private File f;

        FileViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView)itemView.findViewById(R.id.titleView);
            //contentView = (TextView)itemView.findViewById(R.id.contentView);
            dateView = (TextView)itemView.findViewById(R.id.dateView);
            star = (ImageView)itemView.findViewById(R.id.star);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select The Action");
            menu.add(0, v.getId(), 0, "Open");
            menu.add(0, v.getId(), 0, "Rename");
            menu.add(0, v.getId(), 0, "Delete");
            menu.getItem(0).setOnMenuItemClickListener(onFileOpen);
            menu.getItem(1).setOnMenuItemClickListener(onFileRename);
            menu.getItem(2).setOnMenuItemClickListener(onFileDelete);
        }

        private final MenuItem.OnMenuItemClickListener onFileOpen = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
            //activity.itemClick(itemView, getAdapterPosition());
            return true;
            }
        };

        private final MenuItem.OnMenuItemClickListener onFileRename = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
            final EditText editText = new EditText(context);
            editText.setText(f.getName());
            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setMessage("Enter new name for " + f.getName())
                .setView(editText)
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

        private final MenuItem.OnMenuItemClickListener onFileDelete = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setMessage("Delete " + f.getName() + "?")
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
