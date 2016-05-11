package com.csc.simple_todo_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import static com.csc.simple_todo_list.MainActivity.ENTRIES_URI;
import static com.csc.simple_todo_list.ToDoTable.*;

/**
 * Created by anastasia on 11.05.16.
 */
public class EditFragment extends Fragment {
    private Task task;
    private String taskName = "_new";
    EditText titleEdit;
    EditText descriptionEdit;
    EditText addTag;

    public static EditFragment newInstance(String taskName, Context context) {
        EditFragment fragment = new EditFragment();
        if (!taskName.equals("_new")) {
            Cursor cursor = context.getContentResolver().query(ENTRIES_URI, null, "task = ?",
                    new String[]{taskName}, null);
            cursor.moveToNext();
            fragment.task = Task.fromCursor(cursor);
        }
        fragment.taskName = taskName;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_fragment, container, false);

        titleEdit = (EditText) view.findViewById(R.id.titleEdit);
        descriptionEdit = (EditText) view.findViewById(R.id.descriptionEdit);
        addTag = (EditText) view.findViewById(R.id.editTag);
        setHasOptionsMenu(true);
        if(!taskName.equals("_new")) {
            titleEdit.setText(task.title);
            descriptionEdit.setText(task.description);
        } else {
            task = new Task(0, "", "", "", false, false);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                updateData();
            case R.id.action_delete:
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateData() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        final String dateString = sdf.format(System.currentTimeMillis());
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, titleEdit.getText().toString());
        values.put(COLUMN_DESCRIPTION, descriptionEdit.getText().toString());
        values.put(COLUMN_DATE, dateString);
        values.put(COLUMN_STARRED, (task.isStarred? 1 : 0));
        values.put(COLUMN_DONE, (task.isDone?1:0));
        getActivity().getContentResolver().insert(ENTRIES_URI,values);
        getActivity().finish();
    }
}
