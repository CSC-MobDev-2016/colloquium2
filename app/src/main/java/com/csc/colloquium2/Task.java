package com.csc.colloquium2;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;


public class Task implements Comparable<Task> {
    private long id;
    private boolean completed;
    private boolean favorite;
    private String title;
    private String text;
    private String date;

    Task() {
        id = -1;
        completed = false;
        favorite = false;
        this.text = "";
        title = "";
        date = new Date().toString();
    }

    Task(String title, String text) {
        this();
        this.text = text;
    }

    @Override
    public int compareTo(@NonNull Task o) {
        if (!completed == o.completed) {
            return 1;
        }
        if (completed == !o.completed) {
            return -1;
        }
        if (completed == o.completed) {
            if (favorite == !o.favorite) {
                return 1;
            }
            if (!favorite == o.favorite) {
                return -1;
            }
        }
        return date.compareTo(o.date);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
