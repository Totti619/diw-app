package com.antonio.applicacio;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Antonio Ortiz on 01/02/2018.
 */

public class MagatzemPuntuacionsJson implements MagatzemPuntuacions {
    private final Context context;
    private String string; // Emmagatzema puntuacins en format JSON

    public MagatzemPuntuacionsJson(Context context){
        this.context=context;

        string="";
        guardarPuntuacio(45000,"Mi nombre",System.currentTimeMillis());
        guardarPuntuacio(31000,"Otro nombre", System.currentTimeMillis());
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        List<Puntuacio> puntuacions=llegirJSon(string);
        puntuacions.add(new Puntuacio(punts,nom,data));
        string=guardarJSon(puntuacions);
        // guardarString(string);
    }

    @Override
    public Vector<String> llistaPuntuacions(int quntitat) {
        //string=llegirString();
        List<Puntuacio> puntuaciones=llegirJSon(string);
        Vector<String> salida=new Vector<>();
        for(Puntuacio puntuacion:puntuaciones){
            salida.add(puntuacion.getPunts()+" "+puntuacion.getNom());
        }
        return salida;
    }

    private String guardarJSon(List<Puntuacio> puntuacions){
        String string="";
        try{
            JSONArray jsonArray=new JSONArray();
            for(Puntuacio puntuacio:puntuacions) {
                JSONObject objeto=new JSONObject();
                objeto.put("punts",puntuacio.getPunts());
                objeto.put("nom",puntuacio.getNom());
                objeto.put("data",puntuacio.getData());
                jsonArray.put(objeto);
            }
            string=jsonArray.toString();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return string;
    }

    private  List<Puntuacio> llegirJSon(String string){
        List<Puntuacio> puntuacions=new ArrayList<>();
        try{
            JSONArray jsonArray=new JSONArray(string);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject objeto=jsonArray.getJSONObject(i);
                puntuacions.add(new Puntuacio(objeto.getInt("punts"),
                    objeto.getString("nom"), objeto.getLong("data")));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return puntuacions;
    }
}
