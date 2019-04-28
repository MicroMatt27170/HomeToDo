package com.hometodo.BDD;

import android.provider.BaseColumns;

public class CourseContract {
    public static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE IF NOT EXISTS " + CourseEntry.TABLE_NAME + " (" +
                CourseEntry._ID + " INTEGER PRIMARY KEY, " +
                CourseEntry.COLUMN_COURSE_NAME + " TEXT, " +
                CourseEntry.COLUMN_PROFESSOR + " TEXT, " +
                CourseEntry.COLUMN_BEGIN_TIME + " TEXT, " +
                CourseEntry.COLUMN_COLOR + " TEXT, " +
                CourseEntry.COLUMN_DAYS + " INTEGER, " +
                CourseEntry.COLUMN_ROOM + " TEXT, " +
                CourseEntry.COLUMN_CLASS_HOURS + " INTEGER)";

    public CourseContract() {}

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + CourseEntry.TABLE_NAME;

    public static class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_COURSE_NAME = "course_name";
        public static final String COLUMN_PROFESSOR = "professor";
        public static final String COLUMN_BEGIN_TIME = "begin_time";
        public static final String COLUMN_CLASS_HOURS = "class_hours";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_ROOM = "room";
        public static final String COLUMN_DAYS = "days";
    }
}