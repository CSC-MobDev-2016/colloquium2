package com.csc.simple_todo_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by anastasia on 11.05.16.
 */
public class DetailActivity  extends AppCompatActivity implements DetailFragment.OnTaskSelectedListener {
    private String taskName;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        taskName = getIntent().getStringExtra("TASK_TITLE");
        if(taskName.equals("_new")) {
            addEditFragment("_new");
        } else {
            addDetailsFragment(taskName);
        }

    }

    @Override
    public void onTaskSelected(String task) {
        addEditFragment(task);
    }
    private void addDetailsFragment(String task) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_desc_container,
                        DetailFragment.newInstance(task, this))
                //.addToBackStack(null)
                .commit();
    }

    private void addEditFragment(String task) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_desc_container,
                        EditFragment.newInstance(task, this))
                //.addToBackStack(null)
                .commit();
    }
}
