package com.csc.telezhnaya.todo2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private final TaskManager taskManager = TaskManager.INSTANCE;

    @Bind(R.id.list)
    ListView listView;
    @Bind(R.id.new_task)
    EditText newTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ButterKnife.bind(this);
        taskManager.bind(this);
        listView.setAdapter(taskManager.getAdapter());
        getSupportLoaderManager().initLoader(0, null, taskManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskManager.unbind();
    }

    public void onAddClick(View view) {
        String text = newTask.getText().toString();
        if (!text.isEmpty()) {
            newTask.setText("");
            taskManager.addTask(text);
        }
    }

    public void onTaskChanged(View view) {
        View parent = (View) view.getParent();
        TextView taskName = (TextView) parent.findViewById(R.id.task);
        CheckBox checkBox = (CheckBox) parent.findViewById(R.id.task_done);
        ToggleButton star = (ToggleButton) parent.findViewById(R.id.star);
        taskManager.updateTask((int) parent.getTag(), taskName.getText().toString(), null, checkBox.isChecked(), star.isChecked());
    }

    public void onTextClick(View view) {
        View parent = (View) view.getParent();
        final int position = (int) parent.getTag();
        TextView task = (TextView) parent.findViewById(R.id.task);
        CheckBox checkBox = (CheckBox) parent.findViewById(R.id.task_done);
        ToggleButton toggleButton = (ToggleButton) parent.findViewById(R.id.star);
        TextView d = (TextView) parent.findViewById(R.id.creation_date);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = this.getLayoutInflater().inflate(R.layout.edit_task, null);
        dialogBuilder.setView(dialogView);
        final EditText taskName = (EditText) dialogView.findViewById(R.id.task_name);
        taskName.setText(task.getText().toString());
        final EditText taskDescription = (EditText) dialogView.findViewById(R.id.task_description);
        taskDescription.setText(taskManager.getDescription(position));
        TextView date = (TextView) dialogView.findViewById(R.id.creation_date);
        date.setText(d.getText().toString());
        final CheckBox done = (CheckBox) dialogView.findViewById(R.id.task_done);
        done.setChecked(checkBox.isChecked());
        final ToggleButton star = (ToggleButton) dialogView.findViewById(R.id.star);
        star.setChecked(toggleButton.isChecked());


        ListView listView = (ListView) dialogView.findViewById(R.id.tags);
        final TagManager tagManager = new TagManager(position);
        tagManager.bind(this);
        listView.setAdapter(tagManager.getAdapter());
        getSupportLoaderManager().restartLoader(1, null, tagManager);

        Button aggTag = (Button) dialogView.findViewById(R.id.add_tag);
        aggTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText) dialogView.findViewById(R.id.new_tag);
                tagManager.addTag(position, text.getText().toString());
            }
        });


        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newText = taskName.getText().toString();
                if (newText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.write_smth, Toast.LENGTH_SHORT).show();
                } else {
                    String newDescription = taskDescription.getText().toString();
                    taskManager.updateTask(position, newText, newDescription, done.isChecked(), star.isChecked());
                }
            }
        });

        dialogBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        dialogBuilder.create().show();
    }
}
