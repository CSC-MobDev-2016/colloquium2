package ru.ifmo.rain.abduqodir.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
  /**
   * The fragment argument representing the item ID that this fragment
   * represents.
   */
  public static final String ARG_ITEM_ID = "item_id";

  /**
   * The dummy content this fragment is presenting.
   */
  private String mItem;

  ListView tagListView;
  TagCursorAdapter tagCursorAdapter;
  public static final Uri TAG_ENTRIES_URI = Uri.withAppendedPath(WeatherContentProvider.TAG_CONTENT_URI, "entries");

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public ItemDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(ARG_ITEM_ID)) {
      mItem = getArguments().getString(ARG_ITEM_ID);

      Activity activity = this.getActivity();
      CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
      if (appBarLayout != null) {
        appBarLayout.setTitle("Weather");
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.item_detail, container, false);

    TextView taskTitle = (TextView) view.findViewById(R.id.title);
    TextView taskContent = (TextView) view.findViewById(R.id.content);

    Cursor cursor = getActivity().getContentResolver().query(Uri.withAppendedPath(
        ItemListActivity.TASK_ENTRIES_URI, mItem), null, null, null, null);
    assert cursor != null;

    cursor.moveToFirst();

    final String title = cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_TITLE));
    final int starred = cursor.getInt(cursor.getColumnIndex(TaskTable.COLUMN_STARRED));
    final String content = cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_CONTENT));
    final String date = cursor.getString(cursor.getColumnIndex(TaskTable.COLUMN_DATE));
    final int done = cursor.getInt(cursor.getColumnIndex(TaskTable.COLUMN_DONE));
    final String id = cursor.getString(cursor.getColumnIndex(TaskTable._ID));

    taskTitle.setText(title);
    taskContent.setText(content);
    cursor.close();

    taskTitle.setOnLongClickListener(v -> {
          final EditText editNoteContent = new EditText(getContext());
          editNoteContent.setText(title);

          new AlertDialog.Builder(getContext())
              .setTitle("Title")
              .setView(editNoteContent)
              .setPositiveButton("Save", (dialog, which) -> {
                String newTitle = editNoteContent.getText().toString();
                ContentValues values = new ContentValues();
                values.put(TaskTable.COLUMN_TITLE, newTitle);
                values.put(TaskTable.COLUMN_CONTENT, content);
                values.put(TaskTable.COLUMN_DATE, date);
                values.put(TaskTable.COLUMN_DONE, done);
                values.put(TaskTable.COLUMN_STARRED, starred);

                getContext().getContentResolver().update(
                    Uri.withAppendedPath(ItemListActivity.TASK_ENTRIES_URI, id),
                    values,
                    null,
                    null);
              })
              .setNeutralButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
              }).show();
          return true;
        }
    );
    taskContent.setOnLongClickListener(v -> {
          final EditText editNoteContent = new EditText(getContext());
          editNoteContent.setText(content);

          new AlertDialog.Builder(getContext())
              .setTitle("Title")
              .setView(editNoteContent)
              .setPositiveButton("Save", (dialog, which) -> {
                String newContent = editNoteContent.getText().toString();
                ContentValues values = new ContentValues();
                values.put(TaskTable.COLUMN_TITLE, title);
                values.put(TaskTable.COLUMN_CONTENT, newContent);
                values.put(TaskTable.COLUMN_DATE, date);
                values.put(TaskTable.COLUMN_DONE, done);
                values.put(TaskTable.COLUMN_STARRED, starred);

                getContext().getContentResolver().update(
                    Uri.withAppendedPath(ItemListActivity.TASK_ENTRIES_URI, id),
                    values,
                    null,
                    null);
              })
              .setNeutralButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
              }).show();
          return true;
        }
    );

    tagListView = (ListView) view.findViewById(R.id.tags);
    tagCursorAdapter = new TagCursorAdapter(getActivity(), null, 0);

    tagListView.setAdapter(tagCursorAdapter);
    getActivity().getSupportLoaderManager().initLoader(1, null, this);

    return view;
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(getActivity(), TAG_ENTRIES_URI, null, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    tagCursorAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    tagCursorAdapter.swapCursor(null);
  }

  public class TagCursorAdapter extends CursorAdapter {
    public TagCursorAdapter(Context context, Cursor c, int flags) {
      super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
      return new TextView(context);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

      ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(TagTable.COLUMN_TITLE)));
      final String id = cursor.getString(cursor.getColumnIndex(TagTable._ID));
    }
  }
}
