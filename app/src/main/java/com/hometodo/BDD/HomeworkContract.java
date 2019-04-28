package com.hometodo.BDD;
import android.provider.BaseColumns;

public class HomeworkContract {
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + HomeworkEntry.TABLE_NAME + " (" +
                    HomeworkEntry._ID + " INTEGER PRIMARY KEY, " +
                    HomeworkEntry.COLUMN_TITLE + " TEXT, " +
                    HomeworkEntry.COLUMN_COURSE_ID + " INTEGER, " +
                    HomeworkEntry.COLUMN_DATE_DELIVERY + " TEXT, " +
                    HomeworkEntry.COLUMN_DESCRIPTION + " TEXT, " +
                    HomeworkEntry.COLUMN_IS_DONE + " BOOLEAN DEFAULT FALSE)";

    public HomeworkContract() {}

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HomeworkEntry.TABLE_NAME;

    public static class HomeworkEntry implements BaseColumns {
        public static final String TABLE_NAME = "homeworks";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE_DELIVERY = "date_delivery";
        public static final String COLUMN_IS_DONE = "is_done";
    }
}
