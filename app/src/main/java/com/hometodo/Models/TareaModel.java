package com.hometodo.Models;

import android.os.Parcel;
import android.os.Parcelable;


public class TareaModel implements Parcelable {
    public TareaModel(){
    }

    //global variables
    private String nombre;
    private String fecha;
    private String hora;
    private String descripcion;
    private String materia;
    private String color;



    //read the serializable and sets in the global variable
    private TareaModel(Parcel in) {
        nombre = in.readString();
        fecha = in.readString();
        hora = in.readString();
        descripcion = in.readString();
        materia = in.readString();
        color = in.readString();

    }

    //serializable creator it's for internal use of the parcel
    public static final Creator<TareaModel> CREATOR = new Creator<TareaModel>() {
        @Override
        public TareaModel createFromParcel(Parcel in) {
            return new TareaModel(in);
        }

        @Override
        public TareaModel[] newArray(int size) {
            return new TareaModel[size];
        }
    };

    //Methods to set information to the object and get the information
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getMateria() {
        return materia;
    }
    public void setMateria(String materia) {
        this.materia = materia;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    //writes the serializable and sets in the global variable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(fecha);
        dest.writeString(hora);
        dest.writeString(descripcion);
        dest.writeString(materia);
        dest.writeString(color);
    }
}
