package com.csc.tasklist;

import android.net.Uri;

/**
 * Created by Oleg Doronin
 * colloquium2
 * Copyright (c) 2016 CS. All rights reserved.
 */
public class GlobalContext {
    public static final String TASK_ID = "TASK_ID";
    public static final Uri TAGS_URI = Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "tags");
}
