package com.csc.simple_todo_list;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import static com.csc.simple_todo_list.MainActivity.ENTRIES_URI;

/**
 * Created by anastasia on 11.05.16.
 */
public class DetailFragment extends Fragment {
    private Task task;
    private String taskName;

    TextView title;
    TextView description;
    ListView tags;
    ImageView star;
    OnTaskSelectedListener mListener;

    private final ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    };
    public interface OnTaskSelectedListener {
        public void onTaskSelected(String task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTaskSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }
    public static DetailFragment newInstance(String city, Context context) {
        DetailFragment fragment = new DetailFragment();
        Cursor cursor = context.getContentResolver().query(ENTRIES_URI, null, "title = ?",
                new String[] {city}, null);
        cursor.moveToNext();
        fragment.task = Task.fromCursor(cursor);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);

        setHasOptionsMenu(true);
        title = (TextView)view.findViewById(R.id.title);
        description = (TextView)view.findViewById(R.id.description);
        star = (ImageView)view.findViewById(R.id.star);
        title.setText(task.title);
        description.setText(task.description);
        if (task.isStarred) {
            star.setImageResource(R.drawable.star);
        } else {
            star.setImageResource(R.drawable.emptystar);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                mListener.onTaskSelected(taskName);
            case R.id.action_delete:
                getActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
