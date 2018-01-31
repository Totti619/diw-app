package com.antonio.applicacio;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * Created by Antonio Ortiz on 25/01/2018.
 */

public class MagatzemPuntuacionsPreferencies implements MagatzemPuntuacions {

    // Aquest valor servira per donar nom al fitxer xml.
    private static String PREFERENCIES="puntuacions";
    private static String PUNTUACIO="puntuacio";
    private static final int MAX_PUNTUACIONS=10;
    private int contador=0;
    private Context context;

    public MagatzemPuntuacionsPreferencies(Context context) {
        this.context=context;
        this.contador=this.context
                .getSharedPreferences(PREFERENCIES,Context.MODE_PRIVATE)
                .getInt("contador",0);
        if(contador==0)
            this.context
                    .getSharedPreferences(PREFERENCIES,Context.MODE_PRIVATE)
                    .edit()
                    .putInt("contador",0)
                    .commit();
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(PREFERENCIES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        for(int i=MAX_PUNTUACIONS-1;i>0;i--){
            String string1=sharedPreferences.getString(PUNTUACIO+(i-1),"");
            if(!string1.equals(""))
                editor.putString(PUNTUACIO+i,string1).commit();
        }
        editor.putString(PUNTUACIO+"0", punts+" "+nom+" "+data);
        editor.commit();
//        this.contador++;
//        if(this.contador==MAX_PUNTUACIONS)
//            this.contador=0;
//        editor.putInt("contador",this.contador);
//        editor.commit();
    }

    @Override
    public Vector<String> llistaPuntuacions(int quntitat) {
        Vector<String> result=new Vector<String>();
        for(int i=0;i<quntitat;i++){
            String s=context.getSharedPreferences(PREFERENCIES,Context.MODE_PRIVATE)
                    .getString(PUNTUACIO+i,"");
            if (!s.equals("")) {
                result.add(s);
            }
        }

        //Toast.makeText(context,result.toString(),Toast.LENGTH_LONG).show();
        return result;
    }
}
