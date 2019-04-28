package com.hometodo.BDD;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.hometodo.BDD.ExamContract.SQL_CREATE_ENTRIES;
import static com.hometodo.BDD.ExamContract.SQL_DELETE_ENTRIES;

public class ExamDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HomeworkToDo.db";

    public ExamDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.getWritableDatabase().execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HomeworkContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(HomeworkContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
