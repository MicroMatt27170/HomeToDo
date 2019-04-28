package com.hometodo.BDD;

import android.provider.BaseColumns;

public class ExamContract {
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + ExamEntry.TABLE_NAME + " (" +
                    ExamEntry._ID + " INTEGER PRIMARY KEY, " +
                    ExamEntry.COLUMN_TITLE + " TEXT, " +
                    ExamEntry.COLUMN_COURSE_ID + " INTEGER, " +
                    ExamEntry.COLUMN_DATE_DELIVERY + " TEXT, " +
                    ExamEntry.COLUMN_DESCRIPTION + " TEXT, " +
                    ExamEntry.COLUMN_IS_DONE + " BOOLEAN DEFAULT FALSE)";


    public static final String SQL_DELETE_ENTRIES =
            "DROP DATABASE IF EXISTS " + ExamEntry.TABLE_NAME;

    public static class ExamEntry implements BaseColumns {
        public static final String TABLE_NAME = "exams";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE_DELIVERY = "date_delivery";
        public static final String COLUMN_IS_DONE = "is_done";
    }
}
