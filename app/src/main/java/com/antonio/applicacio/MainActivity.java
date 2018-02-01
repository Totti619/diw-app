package com.antonio.applicacio;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends Activity implements
        GestureOverlayView.OnGesturePerformedListener{
    private VistaJoc vistaJoc;
    //private MediaPlayer mp;

    private GestureLibrary llibreria;
    private Button bJugar, bAcercaDe, bConfigurar, bSalir;

    private static int pos=0;

//    public static MagatzemPuntuacions magatzemPuntuacions=new MagatzemPuntuacionsArray();
public static MagatzemPuntuacions magatzemPuntuacions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llibreria= GestureLibraries.fromRawResource(this, R.raw.gestures);
        if(!llibreria.load()) finish();
        GestureOverlayView gesturesView= (GestureOverlayView)findViewById(R.id.gestures);
        // associa escoltador d'event de la gestura en la mateixa classe
        gesturesView.addOnGesturePerformedListener(this);
        bJugar=(Button)findViewById(R.id.buttonJugar);
       // bJugar.setOnClickListener(new MainActivityListener());
        bJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llancarJoc(null);
            }
        });
        bAcercaDe=(Button)findViewById(R.id.buttonAcercaDe);
//        bAcercaDe.setOnClickListener(new View.OnClickListener() {
//           public void onClick(View v) {
//               llancarAcercaDe(null);
//           }
//        });
        bAcercaDe.setOnClickListener(
                new MainActivityListener());
        bConfigurar=(Button)findViewById(R.id.buttonConfigurar);
        bConfigurar.setOnClickListener(new MainActivityListener());
//        bConfigurar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                llancarConfiguracio(null);
//            }
//        });
        bSalir=findViewById(R.id.buttonSalir);
        bSalir.setOnClickListener(new MainActivityListener());
        bSalir.setOnLongClickListener(new MainActivityListener());
//        bSalir.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        bSalir.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                llancarPunctuacions(null);
//                return false;
//            }
//        });
//        bSalir.setBackgroundResource(R.drawable.degradat);

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        //vistaJoc=(VistaJoc)findViewById(R.id.VistaJoc);

//        mp= MediaPlayer.create(this, R.raw.audio);
//        mp.start();
//        mp.seekTo(pos);

        //startService(new Intent(this, ServeiMusica.class));

        // EX 11.2

        magatzemPuntuacions=getMagatzem();
    }

    private MagatzemPuntuacions getMagatzem() {
        SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getString(this
                        .getResources()
                        .getString(R.string.guardar_puntuacions_key)
                ,"1").equals("0") // Array
                )
            return new MagatzemPuntuacionsArray();
        else if(pref.getString(this
                        .getResources()
                        .getString(R.string.guardar_puntuacions_key)
                ,"1").equals("1") // Preferencies
                )
            return new MagatzemPuntuacionsPreferencies(this);
        else if(pref.getString(this
                        .getResources()
                        .getString(R.string.guardar_puntuacions_key)
                ,"1").equals("2") // FitxerIntern
                )
            return new MagatzemPuntuacionsFitxerIntern(this);
        else if(pref.getString(this
                        .getResources()
                        .getString(R.string.guardar_puntuacions_key)
                ,"1").equals("3") // FitxerExtern
                )
            return new MagatzemPuntuacionsFitxerExtern(this);
        else if(pref.getString(this
                        .getResources()
                        .getString(R.string.guardar_puntuacions_key)
                ,"1").equals("4") // XML
                )
            return new MagatzemPuntuacionsXML_SAX(this);
        else if(pref.getString(this
                        .getResources()
                        .getString(R.string.guardar_puntuacions_key)
                ,"1").equals("5") // GSON
                )
            return new MagatzemPuntuacionsGson();
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
//        vistaJoc.getFil().reanudar();
//        mp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mp.pause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
//        vistaJoc.getFil().pausar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
//        vistaJoc.getFil().aturar();
//        this.pos=mp.getCurrentPosition();

        stopService(new Intent(this, ServeiMusica.class));
    }

    //    private class AcercaDeListener implements View.OnClickListener{
//        @Override
//        public void onClick(View view) {
//            llancarAcercaDe(null);
//        }
//    }

    // Metode per llançar l'activitat de Jugar (boto1)
    public void llancarJoc(View view) {
        //view.getContext().startActivity(new Intent(view.getContext(), Joc.class));
        Intent i=new Intent(this, Joc.class);
        // Llança una activitat mitjaçant un objecte Intencio.
        startActivityForResult(i,1234);
    }

    // Metode que es crida de forma automatica quan finalitza
    // l'activitat secundaria. Permet llegir les dades retornades.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1234 && resultCode==RESULT_OK && data!=null) {
            int puntuacio=data.getExtras().getInt("puntuacio");
            String nom="Jo";
            // Millor si ho llegi des d'undialeg o una nova activitat
            // AlertDialog.Builder
            MainActivity.magatzemPuntuacions.guardarPuntuacio(puntuacio, nom,
                    System.currentTimeMillis());
            llancarPunctuacions(null);
        }
    }


//
    public void llancarPunctuacions(View view) {
        startActivity(new Intent(this, Puntuacions.class));
    }
//
//    public void llancarAcercaDe(View view) {
//        Intent i = new Intent(this, AcercaDe.class);
//        startActivity(i);
//    }
//
//    public void llancarConfiguracio(View view) {
//        startActivity(new Intent(this, Preferencies.class));
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Crea l'objecte en java que representa el menú
        MenuInflater infl=getMenuInflater();
        // Associa el menú creat en XML a l'objecte Java
        infl.inflate(R.menu.menu_main, menu);
        // Indica que es vol visualitzar ( activar ) el menú
        return true;
    }
//
//    // Cada vegada que es selecciona el menú es crida el següent mètode
//    // per què tracti els esdeveniments capturats.
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id=item.getItemId();
//        if (id==R.id.config) {
//            // indica que l'event ha sigut tractat i que no s'ha de propagar més.
//            llancarConfiguracio(null);
//            return true;
//        }
//        if (id==R.id.acercaDe) {
//            llancarAcercaDe(null);
//            // Indica que l'event ha sigut tractat i que no s'ha de propagar més.
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
        ArrayList<Prediction> predictions=llibreria.recognize(gesture);
        String predictionName="";
        double predictionScore=0;

        for(Prediction prediction:predictions){
            if(predictionScore<prediction.score){
                predictionScore=prediction.score;
                predictionName=prediction.name;
            }
        }

        switch(predictionName){
            case "acercade":
                startActivity(new Intent(this, AcercaDe.class));
                break;
            case "cancelar":
                System.exit(0);
                break;
            case "configurar":
                startActivity(new Intent(this, Preferencies.class));
                break;
            case "jugar":
                startActivity(new Intent(this, Joc.class));
                break;
            default:
                Toast.makeText(this, "Error...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        MainActivity.pos=mp.getCurrentPosition();
    }
}
