package com.csc.roman_fedorov.todolist;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateNewTaskActivity extends AppCompatActivity {

    public static final Uri TAGS_URI = Uri.withAppendedPath(ToDoTasksContentProvider.CONTENT_URI, "tags");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_task);

        final ArrayList<String> tags = new ArrayList<>();
        Button addTag = (Button) findViewById(R.id.add_tag_button);
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tagEditText = (EditText) findViewById(R.id.add_task_tags_et);
                String tag = tagEditText.getText().toString();
                tags.add(tag);
                tagEditText.setText("");

                TextView allTags = (TextView) findViewById(R.id.current_tags);
                allTags.setText(tags.toString());
            }
        });

        Button createNewTaskButton = (Button) findViewById(R.id.add_new_task_button);
        createNewTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleEditText = (EditText) findViewById(R.id.add_task_title_et);
                String title = titleEditText.getText().toString();

                EditText descriptionEditText = (EditText) findViewById(R.id.add_task_desc_et);
                String description = descriptionEditText.getText().toString();

                CheckBox isFavourite = (CheckBox) findViewById(R.id.mark_as_favourite_checkbox);

                if (description.isEmpty() || title.isEmpty()) {
                    Toast.makeText(CreateNewTaskActivity.this, R.string.empty_fields, Toast.LENGTH_LONG);
                } else {
                    ContentValues newValues = new ContentValues();
                    newValues.put(ToDoTable.COLUMN_TITLE, title);
                    newValues.put(ToDoTable.COLUMN_DESCRIPTION, description);
                    newValues.put(ToDoTable.COLUMN_FAVOURITE, isFavourite.isChecked() ? 1 : 0);
                    Uri uri = getContentResolver().insert(MainActivity.ENTRIES_URI, newValues);
                    int insertedId = (int) ContentUris.parseId(uri);

                    for (String tag : tags) {
                        ContentValues tagValues = new ContentValues();
                        tagValues.put(TagsTable.COLUMN_TASK_ID, insertedId);
                        tagValues.put(TagsTable.COLUMN_TAG, tag);
                        getContentResolver().insert(CreateNewTaskActivity.TAGS_URI, tagValues);
                    }

                    Intent intent = new Intent(CreateNewTaskActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
