package com.hometodo.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Pair;

import com.hometodo.BDD.HomeworkDBHelper;
import com.hometodo.BDD.HomeworkContract.HomeworkEntry;
import com.hometodo.BDD.HomeworkDBHelper;

import java.util.*;


public class Homework extends Model {
    private Long id;
    private Long courseId;
    private String description;
    private String title;
    private String deliveryDate;
    private boolean isDone;

    public Homework()
    {
        onInit();
        this.id = null;
        this.courseId = null;
        this.description = "";
        this.deliveryDate = "";
        this.title = "";
        this.isDone = false;
    }

    public Homework(Long courseId, String description, String title, String deliveryDate, boolean isDone) {
        this.title = title;
        this.isDone = isDone;
        this.description = description;
        this.courseId = courseId;
        this.deliveryDate = deliveryDate;
    }

    public Homework(Long id, Long courseId, String description, String title, String deliveryDate, boolean isDone) {
        this.title = title;
        this.isDone = isDone;
        this.description = description;
        this.id = id;
        this.courseId = courseId;
        this.deliveryDate = deliveryDate;
    }

    private void onInit() {
        String[] h = {HomeworkEntry.COLUMN_IS_DONE,
                HomeworkEntry.COLUMN_DESCRIPTION,
                HomeworkEntry.COLUMN_DATE_DELIVERY,
                HomeworkEntry.COLUMN_COURSE_ID,
                HomeworkEntry.COLUMN_TITLE,
                HomeworkEntry._ID};
        this.setColumns(new ArrayList<String>(Arrays.asList(h)));
        this.setPrimaryKey(HomeworkEntry._ID);
    }

    @Override
    public String getTableName() {
        return HomeworkEntry.TABLE_NAME;
    }

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean save(Context context) {
        ArrayList<Homework> homework = new saveModelInDatabase().doInBackground(new Pair(this, context));
        if(homework == null) return false;
        if(homework.size() > 0) {
            this.id = homework.get(0).getId();

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

    public static class QueryBuilder extends com.hometodo.Models.QueryBuilder<Homework, QueryBuilder>
    {
        public QueryBuilder(Context context) {
            super(context);
        }

        @Override
        public ArrayList<Homework> get() {
            this.QueryConstruct();
            return new getModelInDatabase().doInBackground(this);
        }

        @Override
        public ArrayList<Homework> all() {
            this.setWhere(new ArrayList<WhereClause>());
            this.setOrWhere(new ArrayList<WhereClause>());
            this.setLimit(-1);
            this.setOrderBy("");
            return new getModelInDatabase().doInBackground(this);
        }

        @Override
        public Homework firstOrNull() {
            this.setLimit(1);
            QueryBuilder queryBuilder = new QueryBuilder(context);
            ArrayList<Homework> homeworks = new getModelInDatabase().doInBackground(this);
            if(homeworks.size() > 0) return homeworks.get(0);
            else return null;
        }

    }

    private static class getModelInDatabase extends AsyncTask<QueryBuilder, Void, ArrayList<Homework>> {

        @Override
        protected ArrayList<Homework> doInBackground(QueryBuilder... queryBuilders) {
            QueryBuilder qb = queryBuilders[0];
            HomeworkDBHelper helper = new HomeworkDBHelper(qb.getContext());
            SQLiteDatabase db = helper.getWritableDatabase();

            Homework h = new Homework();
            String[] projection = (h).getColumnsString();

            Cursor cursor = db.query(
                    h.getTableName(),
                    projection,
                    qb.getQuery(),
                    qb.getParamsString(),
                    null,
                    null,
                    qb.getOrderBy(),
                    qb.getLimitString()
            );

            ArrayList<Homework> items = new ArrayList<>();
            while (cursor.moveToNext()) {
                Long c_id = cursor.getLong(cursor.getColumnIndexOrThrow(HomeworkEntry._ID));
                Long c_course_id = cursor.getLong(cursor.getColumnIndexOrThrow(HomeworkEntry.COLUMN_COURSE_ID));
                boolean c_is_done = cursor.getInt(cursor.getColumnIndexOrThrow(HomeworkEntry.COLUMN_IS_DONE)) > 0;
                String c_description = cursor.getString(cursor.getColumnIndexOrThrow(HomeworkEntry.COLUMN_DESCRIPTION));
                String c_title = cursor.getString(cursor.getColumnIndexOrThrow(HomeworkEntry.COLUMN_TITLE));
                String c_delivery_date = cursor.getString(cursor.getColumnIndexOrThrow(HomeworkEntry.COLUMN_DATE_DELIVERY));

                Homework homework = new Homework(c_id, c_course_id, c_description, c_title, c_delivery_date, c_is_done);
                items.add(homework);
            }

            cursor.close();
            return items;
        }
    }

    private static class saveModelInDatabase extends AsyncTask<Pair<Homework, Context>, Void, ArrayList<Homework>> {

        @Override
        protected ArrayList<Homework> doInBackground(Pair<Homework, Context>... pairs) {
            ArrayList<Homework> modelSaved = new ArrayList<>();
            for (Pair<Homework, Context> pair: pairs) {
                try {
                    HomeworkDBHelper helper = new HomeworkDBHelper(pair.second);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    //Create new map of values, where column names are the keys
                    ContentValues values = new ContentValues();

                    values.put(HomeworkEntry.COLUMN_COURSE_ID, pair.first.courseId);
                    values.put(HomeworkEntry.COLUMN_DATE_DELIVERY, pair.first.deliveryDate);
                    values.put(HomeworkEntry.COLUMN_DESCRIPTION, pair.first.description);
                    values.put(HomeworkEntry.COLUMN_IS_DONE, pair.first.isDone);
                    values.put(HomeworkEntry.COLUMN_TITLE, pair.first.title);

                    if(pair.first.id == null) {
                        Long ids = db.insert(HomeworkEntry.TABLE_NAME, HomeworkEntry._ID, values);
                        pair.first.setId(ids);
                        modelSaved.add(pair.first);

                    } else {
                        String[] selectionArgs = { String.valueOf(pair.first.id) };
                        db.update(HomeworkEntry.TABLE_NAME, values, HomeworkEntry._ID + " = ?", selectionArgs);
                    }

                } catch (Exception e) {
                    return null;
                }
            }
            return modelSaved;
        }
    }

    private static class deleteModelInDatabase extends AsyncTask<Pair<Homework, Context>, Void, Integer> {

        @Override
        protected Integer doInBackground(Pair<Homework, Context>... pairs) {
            int deleteRows = 0;
            for (Pair<Homework, Context> pair: pairs) {
                try {
                    HomeworkDBHelper helper = new HomeworkDBHelper(pair.second);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    //Create new map of values, where column names are the keys

                    if(pair.first.id != null) {
                        String[] whereArgs = { pair.first.id.toString() };
                        deleteRows += db.delete(HomeworkEntry.TABLE_NAME, HomeworkEntry._ID + " = ?", whereArgs);
                    }

                } catch (Exception e) {
                    return null;
                }
            }
            return deleteRows;
        }
    }
}
