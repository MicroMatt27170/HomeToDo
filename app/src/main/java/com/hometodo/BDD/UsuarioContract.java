package com.hometodo.BDD;

import android.provider.BaseColumns;

public final class UsuarioContract {

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + UsuarioEntry.TABLE_NAME + " (" +
                    UsuarioEntry._ID + " INTEGER PRIMARY KEY," +
                    UsuarioEntry.NOMBRE_COL + " TEXT," +
                    UsuarioEntry.APPATERNO_COL + " TEXT," +
                    UsuarioEntry.APMATERNO_COL + " TEXT," +
                    UsuarioEntry.SEXO_COL + " TEXT," +
                    UsuarioEntry.ESTADO_COL + " TEXT," +
                    UsuarioEntry.FECHANAC_COL + " TEXT," +
                    UsuarioEntry.CURP_COL + " TEXT," +
                    UsuarioEntry.URL_COL + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UsuarioEntry.TABLE_NAME;

    private UsuarioContract() {}

    /* Inner class that defines the table contents */
    public static class UsuarioEntry implements BaseColumns {
        public static final String TABLE_NAME = "tabla_curp";
        public static final String NOMBRE_COL = "nombre";
        public static final String APPATERNO_COL = "apellidopaterno";
        public static final String APMATERNO_COL = "apellidomaterno";
        public static final String SEXO_COL = "sexo";
        public static final String ESTADO_COL = "estado";
        public static final String FECHANAC_COL = "fechanacimiento";
        public static final String CURP_COL = "curp";
        public static final String URL_COL = "direccion";

    }



}
