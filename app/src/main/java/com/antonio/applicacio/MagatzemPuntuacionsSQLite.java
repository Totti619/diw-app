package com.antonio.applicacio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

/**
 * Created by Antonio Ortiz on 02/02/2018.
 */

public class MagatzemPuntuacionsSQLite extends SQLiteOpenHelper implements MagatzemPuntuacions {

    public MagatzemPuntuacionsSQLite(Context context){
        super(context,"punutacions",null,1);
    }

    @Override
    // Aquest metode nomes es cridara una vegada, quan el sitema
    // detecti que la base dades encara no esta creada.
    public void onCreate(SQLiteDatabase db) {
        // Aqui s'han de crear totes les taules de la BD, i
        // inicialitzar les dades si es necessari.
        // CREATE TABLE nom_taula(nom_columna tipus [atributs], ... )
        db.execSQL("CREATE TABLE vpuntuacions (" + "_id INTEGER PRIMARY "+
            "KEY AUTOINCREMENT," + "punts INTEGER, nom TEXT, data LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En el cas d'una nova versio hauriem d' actualitzar les taules
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        // Obte una referencia a la nostra BD en mode L/E.
        SQLiteDatabase db=getWritableDatabase();
        // Sentencia SQL que afegeix una fila a la taula.
        // INSERT INTO nom_taula VALUES (valor1,valor2, ...)
        // Els valors cadena can entre cometes
        db.execSQL("INSERT INTO vpuntuacions VALUES (null, "+punts+","+
            "'"+nom+"', "+data+")");
        db.close();
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result=new Vector<String>();
        // Obte una referencia a la nostra BD en mode lectura.
        SQLiteDatabase db=getReadableDatabase();
        // Obte un cursor que conte totes les files de la consulta.
        // SELECT columna1,... FROM nom_taula ORDER BY columna [mode]
        // LIMIT numero
        Cursor cursor=db.rawQuery("SELECT punts, nom FROM "+
            "vpuntuacions ORDER BY punts DESC LIMIT "+quantitat,null);
        while(cursor.moveToNext()) {
            result.add(cursor.getInt(0)+" "+cursor.getString(1));
        }
        cursor.close();
        db.close();
        return result;
    }
}
