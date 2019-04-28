package com.hometodo.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Pair;

import com.hometodo.BDD.CourseDBHelper;

import static com.hometodo.BDD.CourseContract.CourseEntry;
import java.util.*;

public class Course extends Model {

    private Long id;
    private String professor;
    private String courseName;
    private String classHours;
    private String beginTime;
    private String room;
    private int days;
    private String color;

    public Course() {
        onInit();
        id = null;
        professor = "";
        classHours = "";
        courseName = "";
        beginTime = "";
        this.color="";
        this.room="";
        this.days=1;
    }

    public Course(String professor, String classHours, String courseName, String beginTime, String color, int days, String room) {
        onInit();
        this.beginTime = beginTime;
        this.courseName = courseName;
        this.classHours = classHours;
        this.professor = professor;
        this.color = color;
        this.id = null;
        this.days = days;
        this.room = room;
    }

    public Course(Long id, String professor, String classHours, String courseName, String beginTime, String color, int days, String room) {
        onInit();
        this.beginTime = beginTime;
        this.courseName = courseName;
        this.classHours = classHours;
        this.professor = professor;
        this.id = id;
        this.color = color;
        this.room = room;
        this.days = days;
    }

    private void onInit() {
        String[] c = {CourseEntry.COLUMN_BEGIN_TIME, CourseEntry.COLUMN_CLASS_HOURS, CourseEntry.COLUMN_COURSE_NAME,
                CourseEntry.COLUMN_PROFESSOR, CourseEntry._ID, CourseEntry.COLUMN_COLOR,
                CourseEntry.COLUMN_ROOM, CourseEntry.COLUMN_DAYS};
        this.setColumns(new ArrayList<String>(Arrays.asList(c)));
        this.setPrimaryKey(CourseEntry._ID);
    }

    @Override
    public String getTableName() {
        return CourseEntry.TABLE_NAME;
    }

    public boolean save(Context context) {
        ArrayList<Course> courses = new saveModelInDatabase().doInBackground(new Pair(this, context));
        if(courses == null) return false;
        if(courses.size() > 0) {
            this.id = courses.get(0).getId();
            return true;
        }
        return false;
    }

    public boolean delete(Context context) {
        Integer deleteRows = new deleteModelInDatabase().doInBackground(new Pair(this, context));
        if(deleteRows == null) return false;
        if(deleteRows > 0) {
            this.id = null;
            return true;
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getProfessor() {
        return professor;
    }

    public int getDays() {
        return days;
    }

    public String getRoom() {
        return room;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getClassHours() {
        return classHours;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public void setClassHours(String classHours) {
        this.classHours = classHours;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public static class QueryBuilder extends com.hometodo.Models.QueryBuilder<Course, QueryBuilder>
    {
        public QueryBuilder(Context context) {
            super(context);
        }

        @Override
        public ArrayList<Course> get() {
            this.QueryConstruct();
            return new getModelInDatabase().doInBackground(this);
        }

        @Override
        public ArrayList<Course> all() {
            this.setWhere(new ArrayList<WhereClause>());
            this.setOrWhere(new ArrayList<WhereClause>());
            this.setLimit(-1);
            this.setOrderBy("");
            return new getModelInDatabase().doInBackground(this);
        }

        @Override
        public Course firstOrNull() {
            this.setLimit(1);
            QueryBuilder queryBuilder = new QueryBuilder(context);
            ArrayList<Course> courses = new getModelInDatabase().doInBackground(this);
            if(courses.size() > 0) return courses.get(0);
            else return null;
        }

    }

    private static class deleteModelInDatabase extends AsyncTask<Pair<Course, Context>, Void, Integer> {

        @Override
        protected Integer doInBackground(Pair<Course, Context>... pairs) {
            int deleteRows = 0;
            for (Pair<Course, Context> pair: pairs) {
                try {
                    CourseDBHelper helper = new CourseDBHelper(pair.second);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    //Create new map of values, where column names are the keys

                    if(pair.first.id != null) {
                        String[] whereArgs = { pair.first.id.toString() };
                        deleteRows += db.delete(CourseEntry.TABLE_NAME, CourseEntry._ID + " = ?", whereArgs);
                    }

                } catch (Exception e) {
                    return null;
                }
            }
            return deleteRows;
        }
    }

    private static class saveModelInDatabase extends AsyncTask<Pair<Course, Context>, Void, ArrayList<Course>> {

        @Override
        protected ArrayList<Course> doInBackground(Pair<Course, Context>... pairs) {
            ArrayList<Course> coursesSaved = new ArrayList<>();
            for (Pair<Course, Context> pair: pairs) {
                try {
                    CourseDBHelper helper = new CourseDBHelper(pair.second);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    //Create new map of values, where column names are the keys
                    ContentValues values = new ContentValues();

                    values.put(CourseEntry.COLUMN_BEGIN_TIME, pair.first.beginTime);
                    values.put(CourseEntry.COLUMN_CLASS_HOURS, pair.first.classHours);
                    values.put(CourseEntry.COLUMN_COURSE_NAME, pair.first.courseName);
                    values.put(CourseEntry.COLUMN_PROFESSOR, pair.first.professor);
                    values.put(CourseEntry.COLUMN_COLOR, pair.first.color);
                    values.put(CourseEntry.COLUMN_ROOM, pair.first.room);
                    values.put(CourseEntry.COLUMN_DAYS, pair.first.days);

                    if(pair.first.id == null) {
                        Long ids = db.insert(CourseEntry.TABLE_NAME, CourseEntry._ID, values);
                        pair.first.setId(ids);
                        coursesSaved.add(pair.first);

                    } else {
                        String[] selectionArgs = { String.valueOf(pair.first.id) };
                        db.update(CourseEntry.TABLE_NAME, values, CourseEntry._ID + " = ?", selectionArgs);
                    }

                } catch (Exception e) {
                    return null;
                }
            }
            return coursesSaved;
        }
    }

    private static class getModelInDatabase extends AsyncTask<QueryBuilder, Void, ArrayList<Course>> {

        @Override
        protected ArrayList<Course> doInBackground(QueryBuilder... queryBuilders) {
            QueryBuilder qb = queryBuilders[0];
            CourseDBHelper helper = new CourseDBHelper(qb.getContext());
            SQLiteDatabase db = helper.getWritableDatabase();

            Course c = new Course();
            String[] projection = (c).getColumnsString();

            Cursor cursor = db.query(
                    c.getTableName(),
                    projection,
                    qb.getQuery(),
                    qb.getParamsString(),
                    null,
                    null,
                    qb.getOrderBy(),
                    qb.getLimitString()
            );

            ArrayList<Course> items = new ArrayList<>();
            while (cursor.moveToNext()) {
                Long c_id = cursor.getLong(cursor.getColumnIndexOrThrow(CourseEntry._ID));
                String c_course_name = cursor.getString(cursor.getColumnIndexOrThrow(CourseEntry.COLUMN_COURSE_NAME));
                String c_begin_time = cursor.getString(cursor.getColumnIndexOrThrow(CourseEntry.COLUMN_BEGIN_TIME));
                String c_class_hour = cursor.getString(cursor.getColumnIndexOrThrow(CourseEntry.COLUMN_CLASS_HOURS));
                String c_professor = cursor.getString(cursor.getColumnIndexOrThrow(CourseEntry.COLUMN_PROFESSOR));
                String c_color = cursor.getString(cursor.getColumnIndexOrThrow(CourseEntry.COLUMN_COLOR));
                String c_room = cursor.getString(cursor.getColumnIndexOrThrow(CourseEntry.COLUMN_ROOM));
                int c_days = cursor.getInt(cursor.getColumnIndexOrThrow(CourseEntry.COLUMN_DAYS));

                Course course = new Course(c_id, c_professor, c_class_hour, c_course_name, c_begin_time, c_color, c_days, c_room);
                items.add(course);
            }

            cursor.close();
            return items;
        }
    }
}
