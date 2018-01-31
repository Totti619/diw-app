package com.antonio.applicacio;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by Antonio Ortiz on 26/01/2018.
 */

public class MagatzemPuntuacionsFitxerExtern implements MagatzemPuntuacions {

    private static String FITXER= Environment.
            getExternalStorageDirectory()+"/puntuacions.txt";
    private static final String STATE=Environment.getExternalStorageState();
    private Context context;

    public MagatzemPuntuacionsFitxerExtern(Context context) {
        this.context=context;
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        if(Environment.MEDIA_MOUNTED.equals(STATE)&&
                !Environment.MEDIA_MOUNTED_READ_ONLY.equals(STATE)) {
            try {
                FileOutputStream f= new FileOutputStream(FITXER,true);
                String text=punts+" "+nom+"\n";
                f.write(text.getBytes());
                f.close();
            } catch (Exception e) {
                Log.e("Asteroider", e.getMessage(), e);
            }
        } else
            Toast.makeText(this.context,"No se puede acceder: "+STATE,Toast.LENGTH_LONG).show();
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result= new Vector<String>();
        try {
            FileInputStream f= new FileInputStream(FITXER);
            BufferedReader entrada= new BufferedReader
                    (new InputStreamReader(f));
            int n=0;
            String linia;
            do {
                linia= entrada.readLine();
                if(linia!=null) {
                    result.add(linia);
                    n++;
                }
            } while(n<quantitat && linia!=null);
            f.close();
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
        return result;
    }
}
