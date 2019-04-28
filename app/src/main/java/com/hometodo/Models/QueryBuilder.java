package com.hometodo.Models;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import java.util.ArrayList;

public class QueryBuilder<type extends Model, builder extends QueryBuilder> {
    private ArrayList<WhereClause> where;
    private ArrayList<WhereClause> orWhere;
    private int limit;
    private String orderBy;
    private String orderBySort;
    protected Context context;
    private String query;
    private ArrayList<String> params;

    public QueryBuilder(Context context) {
        where = new ArrayList<>();
        orWhere = new ArrayList<>();
        limit = -1;
        orderBy = "";
        orderBySort = "ASC";
        this.context = context;
        this.params=new ArrayList<>();
        this.query="";
    }

    public builder where(String column, Object val) {
        where.add(new WhereClause(column, "=", val));
        this.getClass();
        return (builder) this;
    }

    public String getQuery() {
        return query;
    }

    protected void setWhere(ArrayList<WhereClause> where) {
        this.where = where;
    }

    protected void setOrWhere(ArrayList<WhereClause> orWhere) {
        this.orWhere = orWhere;
    }

    protected void setLimit(int limit) {
        this.limit = limit;
    }

    protected void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    protected ArrayList<WhereClause> getOrWhere() {
        return orWhere;
    }

    protected ArrayList<WhereClause> getWhere() {
        return where;
    }

    public Integer getLimit() {
        return (limit < 1) ? null : limit;
    }

    public String getLimitString() {
        return ((limit < 1) ? null : String.valueOf(limit));
    }

    public String getOrderBy() {
        return (orderBy == "") ? null : orderBy + " " + orderBySort;
    }

    public builder where(String column,String clause,Object val) {
        where.add(new WhereClause(column, clause, val));
        return (builder) this;
    }

    public builder orWhere(String column, Object val) {
        orWhere.add(new WhereClause(column, "=", val));
        return (builder) this;
    }

    public builder orWhere(String column, String clause, Object val) {
        orWhere.add(new WhereClause(column, clause, val));
        return (builder) this;
    }

    public builder limit(int n) {
        limit = n < 1 ? 1 : n;
        return (builder) this;
    }

    public builder orderBy(String column) {
        orderBy = column;
        orderBySort = "ASC";
        return (builder) this;
    }

    public builder orderBy(String column, String sort) {
        orderBy = column;
        orderBySort = sort;
        return (builder) this;
    }

    public ArrayList getParams() {
        return params;
    }

    public String[] getParamsString() {
        String[] toString = params.toArray(new String[0]);
        return toString;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }

    public Context getContext() {
        return context;
    }

    public ArrayList<type> get() {
        return new ArrayList<>();
    }

    public  ArrayList<type> all() {
        return new ArrayList<>();
    }

    public type firstOrNull() {
        return null;
    }

    protected class WhereClause {
        private String column;
        private String clause;
        private Object value;

        private WhereClause(String column,String clause, Object value) {
            this.clause = clause;
            this.column = column;
            this.value = value;
        }
    }

    protected void QueryConstruct()
    {
        String query = "";
        ArrayList param = new ArrayList();

        boolean isFirst = true;

        if(where.size() > 0) {
            for (WhereClause w : where) {
                query += isFirst ? " " : " AND ";
                query += w.column + " " + w.clause + " ?";
                param.add(w.value);
                isFirst = false;
            }
        }

        if(orWhere.size() > 0) {
            for (WhereClause w : orWhere) {
                query += isFirst ? " " : " OR ";
                query += w.column + " " + w.clause + " ?";
                param.add(w.value);
                isFirst = false;
            }
        }

        this.query = query;
        this.params = param;
    }
}
