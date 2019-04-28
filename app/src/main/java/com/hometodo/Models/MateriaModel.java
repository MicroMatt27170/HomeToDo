package com.hometodo.Models;

import android.os.Parcel;
import android.os.Parcelable;


public class MateriaModel implements Parcelable {
    public MateriaModel(){
    }

    //global variables
    private String nombre;
    private String dias;
    private String hora;
    private String prof;
    private String salon;
    private String color;



    //read the serializable and sets in the global variable
    private MateriaModel(Parcel in) {
        nombre = in.readString();
        dias = in.readString();
        hora = in.readString();
        prof = in.readString();
        color = in.readString();

    }

    //serializable creator it's for internal use of the parcel
    public static final Creator<MateriaModel> CREATOR = new Creator<MateriaModel>() {
        @Override
        public MateriaModel createFromParcel(Parcel in) {
            return new MateriaModel(in);
        }

        @Override
        public MateriaModel[] newArray(int size) {
            return new MateriaModel[size];
        }
    };

    //Methods to set information to the object and get the information
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDias() {
        return dias;
    }
    public void setDias(String dias) {
        this.dias = dias;
    }
    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }
    public String getProf() {
        return prof;
    }
    public void setProf(String prof) {
        this.prof = prof;
    }
    public String getSalon() {
        return salon;
    }
    public void setSalon(String salon) {
        this.salon = salon;
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
        dest.writeString(dias);
        dest.writeString(hora);
        dest.writeString(prof);
        dest.writeString(salon);
        dest.writeString(color);
    }
}
