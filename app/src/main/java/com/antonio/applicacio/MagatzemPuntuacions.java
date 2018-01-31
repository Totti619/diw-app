package com.antonio.applicacio;

import java.util.Vector;

/**
 * Created by Master on 05/11/2017.
 */

public interface MagatzemPuntuacions {
    public void guardarPuntuacio(int punts, String nom, long data);
    public Vector<String> llistaPuntuacions(int quntitat);
}
