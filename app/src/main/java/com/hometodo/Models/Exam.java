package com.hometodo.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Pair;

import com.hometodo.BDD.ExamContract;
import com.hometodo.BDD.ExamDBHelper;
import com.hometodo.BDD.ExamContract.ExamEntry;
import com.hometodo.BDD.ExamDBHelper;

import java.util.*;

public class Exam extends Model{
    private Long id;
    private Long courseId;
    private String title;
    private String description;
    private String dateDelivery;
    private boolean isDone;

    public Exam() {
        onInit();
        this.id = null;
        this.courseId = null;
        this.title = "";
        this.description = "";
        this.dateDelivery = "";
        this.isDone = false;
    }

    public Exam(Long courseId,String title,String description,String dateDelivery,boolean isDone) {
        //onInit();
        //this.id = null;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.dateDelivery = dateDelivery;
        this.isDone = isDone;
    }

    public Exam(Long id,Long courseId,String title,String description,String dateDelivery,boolean isDone) {
        //onInit();
        this.id = id;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.dateDelivery = dateDelivery;
        this.isDone = isDone;
    }

    private void onInit() {
        String[] c = {ExamContract.ExamEntry.COLUMN_TITLE,
                ExamContract.ExamEntry.COLUMN_IS_DONE,
                ExamContract.ExamEntry.COLUMN_DESCRIPTION,
                ExamContract.ExamEntry.COLUMN_DATE_DELIVERY,
                ExamContract.ExamEntry.COLUMN_COURSE_ID,
                ExamContract.ExamEntry._ID};
        this.setColumns(new ArrayList<String>(Arrays.asList(c)));
        this.setPrimaryKey(ExamContract.ExamEntry._ID);
    }

    @Override
    public String getTableName() {
        return ExamContract.ExamEntry.TABLE_NAME;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getCourseId() {
        return courseId;
    }

    public Long getId() {
        return id;
    }

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public boolean save(Context context) {
        ArrayList<Exam> exam = new saveModelInDatabase().doInBackground(new Pair(this, context));
        if(exam == null) return false;
        if(exam.size() > 0) {
            this.id = exam.get(0).getId();

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

    public static class QueryBuilder extends com.hometodo.Models.QueryBuilder<Exam, QueryBuilder>
    {
        public QueryBuilder(Context context) {
            super(context);
        }

        @Override
        public ArrayList<Exam> get() {
            this.QueryConstruct();
            return new getModelInDatabase().doInBackground(this);
        }

        @Override
        public ArrayList<Exam> all() {
            this.setWhere(new ArrayList<WhereClause>());
            this.setOrWhere(new ArrayList<WhereClause>());
            this.setLimit(-1);
            this.setOrderBy("");
            return new getModelInDatabase().doInBackground(this);
        }

        @Override
        public Exam firstOrNull() {
            this.setLimit(1);
            QueryBuilder queryBuilder = new QueryBuilder(context);
            ArrayList<Exam> exams = new getModelInDatabase().doInBackground(this);
            if(exams.size() > 0) return exams.get(0);
            else return null;
        }

    }

    private static class getModelInDatabase extends AsyncTask<QueryBuilder, Void, ArrayList<Exam>> {

        @Override
        protected ArrayList<Exam> doInBackground(QueryBuilder... queryBuilders) {
            QueryBuilder qb = queryBuilders[0];
            ExamDBHelper helper = new ExamDBHelper(qb.getContext());
            SQLiteDatabase db = helper.getWritableDatabase();

            Exam h = new Exam();
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

            ArrayList<Exam> items = new ArrayList<>();
            while (cursor.moveToNext()) {
                Long c_id = cursor.getLong(cursor.getColumnIndexOrThrow(ExamEntry._ID));
                Long c_course_id = cursor.getLong(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_COURSE_ID));
                boolean c_is_done = cursor.getInt(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_IS_DONE)) > 0;
                String c_description = cursor.getString(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_DESCRIPTION));
                String c_title = cursor.getString(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_TITLE));
                String c_delivery_date = cursor.getString(cursor.getColumnIndexOrThrow(ExamEntry.COLUMN_DATE_DELIVERY));

                Exam exam = new Exam(c_id, c_course_id, c_description, c_title, c_delivery_date, c_is_done);
                items.add(exam);
            }

            cursor.close();
            return items;
        }
    }

    private static class saveModelInDatabase extends AsyncTask<Pair<Exam, Context>, Void, ArrayList<Exam>> {

        @Override
        protected ArrayList<Exam> doInBackground(Pair<Exam, Context>... pairs) {
            ArrayList<Exam> modelSaved = new ArrayList<>();
            for (Pair<Exam, Context> pair: pairs) {
                try {
                    ExamDBHelper helper = new ExamDBHelper(pair.second);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    //Create new map of values, where column names are the keys
                    ContentValues values = new ContentValues();

                    values.put(ExamEntry.COLUMN_COURSE_ID, pair.first.courseId);
                    values.put(ExamEntry.COLUMN_DATE_DELIVERY, pair.first.dateDelivery);
                    values.put(ExamEntry.COLUMN_DESCRIPTION, pair.first.description);
                    values.put(ExamEntry.COLUMN_IS_DONE, pair.first.isDone);
                    values.put(ExamEntry.COLUMN_TITLE, pair.first.title);

                    if(pair.first.id == null) {
                        Long ids = db.insert(ExamEntry.TABLE_NAME, ExamEntry._ID, values);
                        pair.first.setId(ids);
                        modelSaved.add(pair.first);

                    } else {
                        String[] selectionArgs = { String.valueOf(pair.first.id) };
                        db.update(ExamEntry.TABLE_NAME, values, ExamEntry._ID + " = ?", selectionArgs);
                    }

                } catch (Exception e) {
                    return null;
                }
            }
            return modelSaved;
        }
    }

    private static class deleteModelInDatabase extends AsyncTask<Pair<Exam, Context>, Void, Integer> {

        @Override
        protected Integer doInBackground(Pair<Exam, Context>... pairs) {
            int deleteRows = 0;
            for (Pair<Exam, Context> pair: pairs) {
                try {
                    ExamDBHelper helper = new ExamDBHelper(pair.second);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    //Create new map of values, where column names are the keys

                    if(pair.first.id != null) {
                        String[] whereArgs = { pair.first.id.toString() };
                        deleteRows += db.delete(ExamEntry.TABLE_NAME, ExamEntry._ID + " = ?", whereArgs);
                    }

                } catch (Exception e) {
                    return null;
                }
            }
            return deleteRows;
        }
    }
}
