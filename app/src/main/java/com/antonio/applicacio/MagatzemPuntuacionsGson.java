package com.antonio.applicacio;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Antonio Ortiz on 01/02/2018.
 */

public class MagatzemPuntuacionsGson implements MagatzemPuntuacions {
    private final Context context;
    private String string; // Emmagatzema puntuacions en format JSON
    private Gson gson= new Gson();
    private Type type=new TypeToken<Clase>() {}.getType();

    private static final String FITXER="puntuacions.json";

    public MagatzemPuntuacionsGson(Context context) {
        this.context=context;

        // Inicialitza uns valors
        guardarPuntuacio(45000,"Mi nombre",System.currentTimeMillis());
        guardarPuntuacio(31000,"Otro nombre", System.currentTimeMillis());
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        string = llegirString();
        Clase obj;
        if(string==null)
            obj=new Clase();
        else
            obj=gson.fromJson(string,type);
        obj.puntuacions.add(
                new Puntuacio(punts,nom,data)
        );
        string=gson.toJson(obj, type);
        guardarString(string);
    }

    private String llegirString() {
        StringBuffer string=new StringBuffer();
        try{
            FileInputStream fis=context.openFileInput(FITXER);
            int c;
            while ((c=fis.read())!=-1)
                string.append((char)c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("json",string.toString());
        return string.toString();
    }
    private void guardarString(String string) {
        try{
            FileOutputStream fos=context.openFileOutput(FITXER, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                new File(FITXER).createNewFile();
                guardarString(string);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int cantidad) {
        string= llegirString();
        Clase objeto=gson.fromJson(string,type);
        Vector<String> salida=new Vector<>();
        for(Puntuacio puntuacio:objeto.puntuacions) {
            salida.add(puntuacio.getPunts()+" "+puntuacio.getNom());
        }
        return salida;
    }

    public class Clase {
        private ArrayList<Puntuacio> puntuacions=new ArrayList<>();
        private boolean guardad;
    }
}
