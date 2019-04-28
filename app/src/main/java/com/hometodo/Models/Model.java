package com.hometodo.Models;

import java.util.ArrayList;

public class Model {
    private static ArrayList<String> columns;
    private static String primaryKey = "_id";
    private static String tableName = null;

    public Model(ArrayList<String> columns,String primaryKey,String tableName) {
        this.columns = columns;
        this.primaryKey = primaryKey;
        this.tableName = tableName;
    }

    public Model() { }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public String[] getColumnsString() {
        String[] toString = columns.toArray(new String[0]);
        return toString;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}