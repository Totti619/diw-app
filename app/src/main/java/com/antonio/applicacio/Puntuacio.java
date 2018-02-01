package com.antonio.applicacio;

/**
 * Created by Antonio Ortiz on 01/02/2018.
 */

public class Puntuacio {
    private int punts;
    private String nom;
    private long data;
    public Puntuacio(int puntus, String nom, long data) {
        this.punts=punts;
        this.nom=nom;
        this.data=data;
    }

    public int getPunts() {
        return punts;
    }

    public void setPunts(int punts) {
        this.punts = punts;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
}
