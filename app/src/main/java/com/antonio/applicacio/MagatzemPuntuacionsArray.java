package com.antonio.applicacio;

import java.util.Vector;

/**
 * Created by Master on 05/11/2017.
 */

public class MagatzemPuntuacionsArray implements MagatzemPuntuacions {
    private Vector<String> puntuacions;

    public MagatzemPuntuacionsArray() {
        this.puntuacions = new Vector<String>();
//        puntuacions.add("123000 Pepito Dominquez");
//        puntuacions.add("111000 Pedro Martinez");
//        puntuacions.add("011000 Paco Perez");
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        puntuacions.add(punts+" "+nom+" "+data);
    }

    @Override
    public Vector<String> llistaPuntuacions(int quntitat) {
        return puntuacions;
    }
}
