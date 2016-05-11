package ru.ifmo.rain.abduqodir.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  public static final Uri TASK_ENTRIES_URI = Uri.withAppendedPath(WeatherContentProvider.TASK_CONTENT_URI, "entries");
  public static final Uri TAG_ENTRIES_URI = Uri.withAppendedPath(WeatherContentProvider.TAG_CONTENT_URI, "entries");
  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private boolean mTwoPane;
  ListView weatherListView;
  TaskCursorAdapter taskCursorAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_list);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
    assert fab != null;
    fab.setOnClickListener(v -> {
      final EditText editNoteContent = new EditText(this);
      new AlertDialog.Builder(this)
          .setTitle("Add Task")
          .setView(editNoteContent)
          .setPositiveButton("Add", (dialog, which) -> {
            String newTask = editNoteContent.getText().toString();
            Utils.addTask(this, newTask);
          })
          .setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
          }).show();
    });

    weatherListView = (ListView) findViewById(R.id.item_list);
    taskCursorAdapter = new TaskCursorAdapter(this, null, 0);

    weatherListView.setAdapter(taskCursorAdapter);
    getSupportLoaderManager().initLoader(0, null, this);

    if (findViewById(R.id.item_detail_container) != null) {
      mTwoPane = true;
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, TASK_ENTRIES_URI, null, null, null, WeatherContentProvider.TASK_SORT_ORDER);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    taskCursorAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    taskCursorAdapter.swapCursor(null);
  }

  public class TaskCursorAdapter extends CursorAdapter {
    public TaskCursorAdapter(Context context, Cursor c, int flags) {
      super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
      return LayoutInflater.from(context).inflate(R.layout.item_list_content, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

      final CheckBox buttonStar = (CheckBox) view.findViewById(R.id.btn_star);
      final TextView noteTitle = (TextView) view.findViewById(R.id.note_title);
      final TextView noteDate = (TextView) view.findViewById(R.id.note_date);
      final CheckBox buttonDone = (CheckBox) view.findViewById(R.id.btn_done);

      final int starred = cursor.getInt(cursor.getColumnIndex(TaskTable.COLUMN_STARRED));
      final String title = cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_TITLE));
      final String content = cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_CONTENT));
      final String date = cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_DATE));
      final int done = cursor.getInt(cursor.getColumnIndex(TaskTable.COLUMN_DONE));
      final String id = cursor.getString(cursor.getColumnIndex(TaskTable._ID));

      buttonStar.setChecked(starred == 1);
      noteTitle.setText(title);
      noteDate.setText(date);
      buttonDone.setChecked(done == 1);


      if (buttonDone.isChecked()) {
        noteTitle.setPaintFlags(noteTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      } else {
        noteTitle.setPaintFlags(noteTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
      }

      buttonDone.setOnClickListener(v -> {
        ContentValues values = new ContentValues();
        values.put(TaskTable.COLUMN_TITLE, title);
        values.put(TaskTable.COLUMN_CONTENT, content);
        values.put(TaskTable.COLUMN_DATE, date);
        values.put(TaskTable.COLUMN_STARRED, starred);

        values.put(TaskTable.COLUMN_DONE, buttonDone.isChecked() ? 1 : 0);

        context.getContentResolver().update(Uri.withAppendedPath(ItemListActivity.TASK_ENTRIES_URI, id),
            values, null, null);
      });


      buttonStar.setOnClickListener(v -> {
        ContentValues values = new ContentValues();
        values.put(TaskTable.COLUMN_TITLE, title);
        values.put(TaskTable.COLUMN_CONTENT, content);
        values.put(TaskTable.COLUMN_DATE, date);

        values.put(TaskTable.COLUMN_STARRED, buttonStar.isChecked() ? 1 : 0);

        values.put(TaskTable.COLUMN_DONE, done);

        context.getContentResolver().update(Uri.withAppendedPath(ItemListActivity.TASK_ENTRIES_URI, id),
            values, null, null);
      });

      view.setOnClickListener(v -> {
        if (mTwoPane) {
          Bundle arguments = new Bundle();
          arguments.putString(ItemDetailFragment.ARG_ITEM_ID, id);
          ItemDetailFragment fragment = new ItemDetailFragment();
          fragment.setArguments(arguments);
          getSupportFragmentManager().beginTransaction()
              .replace(R.id.item_detail_container, fragment)
              .commit();
        } else {
          Intent intent = new Intent(context, ItemDetailActivity.class);
          intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, id);

          context.startActivity(intent);
        }
      });
    }
  }
}
