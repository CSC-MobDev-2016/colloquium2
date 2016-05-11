package ru.ifmo.rain.abduqodir.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observable;

/**
 * Created by abduqodir on 11.05.16.
 */
public class Utils {

  public static void updateTaskTitle(Context context, String taskId, String newTaskTitle) {
  }
  public static void updateTaskContent(Context context, String taskId, String newTaskDescription) {
  }
  public static void addTaskTag(Context context, String taskId, String tag) {
  }
  public static void deleteTaskTag(Context context, String TaskId, String tagId) {
  }

  public static void addTask(Context context, String taskTitle) {
    ContentValues values = new ContentValues();
    SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.date_format), Locale.getDefault());

    values.put(TaskTable.COLUMN_TITLE, String.valueOf(taskTitle));
    values.put(TaskTable.COLUMN_DATE, format.format(new Date()));
    values.put(TaskTable.COLUMN_DONE, 0);
    values.put(TaskTable.COLUMN_STARRED, 0);
    context.getContentResolver().insert(ItemListActivity.TASK_ENTRIES_URI, values);
  }
}
