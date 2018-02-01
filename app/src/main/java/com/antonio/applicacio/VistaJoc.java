package com.antonio.applicacio;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Antonio Ortiz on 11/11/2017.
 */

public class VistaJoc extends View implements SensorEventListener{
    private SensorManager mSensorManager;

    // Objecte que permet accedir a l'activitat que crida al Layout.
    private Activity pare;

    public void setPare(Activity pare) {
        this.pare=pare;
    }

    //MEtode que permet finalitzar el joc retornant la puntuacio
    private void sortir() {
        Bundle bundle=new Bundle();
        bundle.putInt("puntuacio", puntuacio);
        Intent intent=new Intent();
        intent.putExtras(bundle);
        pare.setResult(Activity.RESULT_OK, intent);
        pare.finish();
    }

    // Variable que conte la puntuacio del joc
    private int puntuacio=0;

    // Variables perl so
    MediaPlayer mpDispar, mpExplosio;

    // Variables pel Missil
    //private Grafic missil;
    private Grafic missils;
    private static int PAS_VELOCITAT_MISSIL=12;
    private boolean missilActiu=false;
    private int tempsMissil;
    // FILS I TEMPS
    // Fil encarregat de processar el joc
    private ThreadJoc fil=new ThreadJoc();
    // Cada quan volem processar canvis (ms)
    private static int PERIODE_PROCES=50;
    // Quan es va realitzar el darrer procés
    private long darrerProces=0;
    // Variables per la NAU
    private Grafic nau; // Gràfic de la nau
    private int girNau; // Increment de direció
    private double acceleracioNau; // Augment de velocitat
    private static final int MAX_VELOCITAT_NAU=20;
    // increment estandar de gir i accelaració
    private static final int PAS_GIR_NAU=5;
    private static final float PAS_ACCELERACIO_NAU=0.5f;

    // Variables per ASTEROIDES
    private Vector<Grafic> asteroides; // Vector amb els asteroides
    private Vector<GraficMisil> vmissiles;
    private int numAsteroides=5; // Numero inicial de Asteroides
    private int numFragments=3; // Fragments en que es divideix
    private int numMissils=50;

    private Drawable drawableAsteroide[]=new Drawable[numFragments];
    private Drawable drawableMissil;

    public ThreadJoc getFil() {
        return this.fil;
    }

    public VistaJoc(Context context, AttributeSet attrs) {
        super(context, attrs);

        vmissiles=new Vector<GraficMisil>();
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getContext());

        // Ex 9_1
        numFragments=Integer.parseInt(pref.getString(getResources().getString(R.string.pa3_key),"3"));


        numMissils=Integer.parseInt(pref.getString(getResources().getString(R.string.missils_key),"50"));

        // Registre el sensor d'orientació i indica gestió d'events.
        mSensorManager=(SensorManager)context.
                getSystemService(Context.SENSOR_SERVICE);
        int sensorType=0;
        if(pref.getString(getResources().getString(R.string.pa4_key), "1").equals("0"))
            sensorType=Sensor.TYPE_ACCELEROMETER;
        if(pref.getString(getResources().getString(R.string.pa4_key), "1").equals("1"))
            sensorType=Sensor.TYPE_ORIENTATION;
        List<Sensor> llistaSensors= mSensorManager.
                getSensorList(sensorType);
        if(!llistaSensors.isEmpty()){
            Sensor orientacioSensor=llistaSensors.get(0);
            mSensorManager.registerListener(this, orientacioSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }

        // Declara i obte les imatges
        Drawable drawableNau;
        drawableNau=context.getResources().getDrawable(R.drawable.nau);
//        drawableAsteroide=context.getResources().getDrawable(R.drawable.asteroide1);

        // Obté les preferencies del contexte
        if(pref.getString(getResources().getString(R.string.pa2_key), "1").equals("0")) {
            // Gràfic vectorial pel Missilfor(int i=0;i<numMissils;i++) {
                ShapeDrawable dMissil=new ShapeDrawable(new RectShape());
                dMissil.getPaint().setColor(Color.WHITE);
                dMissil.getPaint().setStyle(Paint.Style.STROKE);
                dMissil.setIntrinsicWidth(15);
                dMissil.setIntrinsicHeight(3);
                drawableMissil=dMissil;
           // }

            Path pathAsteroide= new Path();
            pathAsteroide.moveTo((float)0.3, (float)0.0);
            pathAsteroide.lineTo((float)0.6, (float)0.0);
            pathAsteroide.lineTo((float)0.6, (float)0.3);
            pathAsteroide.lineTo((float)0.8, (float)0.2);
            pathAsteroide.lineTo((float)1.0, (float)0.4);
            pathAsteroide.lineTo((float)0.8, (float)0.6);
            pathAsteroide.lineTo((float)0.9, (float)0.9);
            pathAsteroide.lineTo((float)0.8, (float)1.0);
            pathAsteroide.lineTo((float)0.4, (float)1.0);
            pathAsteroide.lineTo((float)0.0, (float)0.6);
            pathAsteroide.lineTo((float)0.0, (float)0.2);
            pathAsteroide.lineTo((float)0.3, (float)0.0);
//            ShapeDrawable dAsteroide= new ShapeDrawable(new PathShape(pathAsteroide,1,1));
//            dAsteroide.getPaint().setColor(Color.WHITE);
//            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
//            dAsteroide.setIntrinsicWidth(50);
//            dAsteroide.setIntrinsicHeight(50);
//            drawableAsteroide=dAsteroide;
            for(int i=0; i<numFragments; i++) {
                ShapeDrawable dAsteroide= new ShapeDrawable(
                        new PathShape(pathAsteroide,1,1)
                );
                dAsteroide.getPaint().setColor(Color.WHITE);
                dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
                dAsteroide.setIntrinsicWidth(50-i*14);
                dAsteroide.setIntrinsicHeight(50-i*14);
                drawableAsteroide[i]=dAsteroide;
            }
            setBackgroundColor(Color.BLACK);

            Path pathNau= new Path();
            pathNau.moveTo((float)0.0, (float)0.0);
            pathNau.lineTo((float)1.0, (float)0.5);
            pathNau.lineTo((float)0.0, (float)1.0);
            pathNau.lineTo((float)0.0, (float)0.0);
            ShapeDrawable dNau= new ShapeDrawable(new PathShape(pathNau,1,1));
            dNau.setIntrinsicWidth(20);
            dNau.setIntrinsicHeight(15);
            drawableNau=dNau;
        } else {
            drawableAsteroide[0] = context.getResources().getDrawable(R.drawable.asteroide1);
            drawableAsteroide[1] = context.getResources().getDrawable(R.drawable.asteroide2);
            drawableAsteroide[2] = context.getResources().getDrawable(R.drawable.asteroide3);
            //for(int i=0;i<numMissils;i++)
                drawableMissil=context.getResources().getDrawable(R.drawable.missil1);
        }
        // Inicialitza els asteroides
        asteroides=new Vector<Grafic>();
        nau=new Grafic(this, drawableNau);
        //missil=new Grafic(this, drawableMissil);

        for(int i=0; i<numAsteroides; i++) {
            //7for(int j=0;j<numFragments;j++){
                Grafic asteroide= new Grafic(this, drawableAsteroide[0]);
                asteroide.setIncY(Math.random()*4-2);
                asteroide.setIncX(Math.random()*4-2);
                asteroide.setAngle((int)(Math.random()*360));
                asteroide.setRotacio((int)(Math.random()*8-4));
                asteroides.add(asteroide);
            //}
        }

        mpDispar=MediaPlayer.create(context, R.raw.dispar);
        mpExplosio=MediaPlayer.create(context, R.raw.explosio);
    }

    // Manejador d'events de la pantalla tactil per la nau
    private float mX=0, mY=0;
    private boolean dispar=false;

    // GESTIO D'EVENTS DE LA NAU AMB PANTALLA TACTIL
    @Override
    public boolean onTouchEvent(MotionEvent mevent){
        super.onTouchEvent(mevent);
        float x=mevent.getX();
        float y=mevent.getY();
        switch(mevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                dispar=true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx=Math.abs(x-mX);
                float dy=Math.abs(y-mY);
                if(dy<6 && dx>6){
                    girNau=Math.round((x-mX)/2);
                    dispar=false;
                } else if(dx<6 && dy>6){
                    acceleracioNau=Math.round((x-mX)/2);
                    dispar=false;
                }
                break;
            case MotionEvent.ACTION_UP:
                girNau=0;
                acceleracioNau=0;
                if(dispar){
                    ActivaMissil();
                }
                break;
        }
        mX=x; mY=y;
        return true;
    }

    @Override
    protected void onSizeChanged(int ample, int alt, int ampla_ant, int alt_ant) {
        super.onSizeChanged(ample, alt, ampla_ant, alt_ant);
        nau.setCenY(alt/2);
        nau.setCenX(ample/2);
        // una vegada que coneixem la nostra amplada i altura posiciona els asteroides
        for(Grafic asteroide: asteroides) {
            do {
                asteroide.setCenX((int)(Math.random()*ample));
                asteroide.setCenY((int)(Math.random()*alt));
            } while (asteroide.distancia(nau) < (ample+alt)/5);
        }
        darrerProces= System.currentTimeMillis();
        fil.start();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(Grafic misil:vmissiles)
            misil.dibuixaGrafic(canvas);
        nau.dibuixaGrafic(canvas);
        // Dibuixa els asteroides
        for(Grafic asteroide: asteroides)
            asteroide.dibuixaGrafic(canvas);
    }

    // ACTUALITZA ELS VALORS DELS ELEMENTS
    // ÉS A DIR, GESTIONA ELS MOVIMENTS
    protected synchronized void actualitzaFisica() {
        // Hora actual en milisegons
        long ara=System.currentTimeMillis();
        // No fer res si el periode de proces NO s'ha complert
        if ( darrerProces+PERIODE_PROCES > ara ) {
            return;
        }
        // Per una execució en temps real calculem retard
        double retard=(ara-darrerProces)/PERIODE_PROCES;
        darrerProces=ara; // Per la propera vegada
        // Actualitzame velocitat i direcció de la nau a partir de
        // girNau i acceleracioNau segons l'entrada del jugador
        nau.setAngle((int)(nau.getAngle()+girNau*retard));
        double nIncX= nau.getIncX()+acceleracioNau*Math.cos(Math.toRadians(nau.getAngle()))*retard;
        double nIncY= nau.getIncY()+acceleracioNau+Math.sin(Math.toRadians(nau.getAngle()))*retard;

        // Actualitzem si el mòdul de la velocitat no passa el màxim
        if (Math.hypot(nIncX, nIncY) <= MAX_VELOCITAT_NAU) {
            nau.setIncX(nIncX);
            nau.setIncY(nIncY);
        }
        // Actualitzem les posicions X i Y
        nau.incrementaPos(retard);
        for(int i=0; i<asteroides.size(); i++) {
            asteroides.get(i).incrementaPos(retard);
        }

        // Actualizem posicio del missil
        for(GraficMisil missil:vmissiles) if(missil.isMisilActiu()){
            missil.incrementaPos(retard);
            tempsMissil-=retard;
            if(tempsMissil<0){
                missil.setMisilActiu(false);
            }else{
                for(int i=0; i<asteroides.size(); i++){
                    if(missil.verificaColisio(asteroides.get(i))){
                        destrueixAsteroide(i);
                        vmissiles.remove(missil);
                        break;
                    }
                }
            }
        }
        for(Grafic asteroide: asteroides) {
            if(asteroide.verificaColisio(nau)){
                sortir();
            }
        }
    }

    private void destrueixAsteroide(int i) {
        puntuacio+=1000;
        int tam;
        if(asteroides.get(i).getDrawable()!=drawableAsteroide[2]){
            if(asteroides.get(i).getDrawable()==drawableAsteroide[1]){
                tam=2;
            }else{
                tam=1;
            }
            for(int n=0; n<numFragments;n++){
                Grafic asteroide=new Grafic(this,
                        drawableAsteroide[tam]);
                asteroide.setCenX(asteroides.get(i).getCenX());
                asteroide.setCenY(asteroides.get(i).getCenY());
                asteroide.setIncX(Math.random()*7-2-tam);
                asteroide.setIncY(Math.random()*7-2-tam);
                asteroide.setAngle((int)(Math.random()*360));
                asteroide.setRotacio((int)(Math.random()*8-4));
                asteroides.add(asteroide);
            }
        }
        asteroides.remove(i);
        mpExplosio.start();

        // Si no queda cap asteroides en pantalla finalitzar partida
        if(asteroides.isEmpty()) {
            sortir();
        }
    }

    private synchronized void ActivaMissil() {
            GraficMisil missil=new GraficMisil(this, drawableMissil);
            missil.setCenX(nau.getCenX());
            missil.setCenY(nau.getCenY());
            missil.setAngle(nau.getAngle());
            missil.setIncX(Math.cos(Math.toRadians(missil.getAngle()))*
                    PAS_VELOCITAT_MISSIL);
            tempsMissil= (int)Math.min(
                    this.getWidth()/Math.abs(missil.getIncX()),
                    this.getHeight()/Math.abs(missil.getIncY())
            )-2;

            mpDispar.start();
            vmissiles.add(missil);
    }

    @Override
    // GESTIÓ D'EVENTS
    public boolean onKeyDown(int codiTecla, KeyEvent event){
        super.onKeyDown(codiTecla, event);
        boolean procesada=true;
        // Suposem que processem la pulsacio
        switch(codiTecla){
            case KeyEvent.KEYCODE_DPAD_UP:
                acceleracioNau=+PAS_ACCELERACIO_NAU;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                acceleracioNau=-PAS_GIR_NAU;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                acceleracioNau=+PAS_GIR_NAU;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                //ActivaMisil();
                break;
            default:
                // Si arriba aqui, no hi ha pulsació que interesi
                procesada=false;
                break;
        }
        return procesada; // Hem procesat l'event
    }

    // GESTIÓ D'EVENTS DE SENSORS PER LA NAU
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private boolean hihaValorInicial=false;
    private float initX, initY, initZ;


    @Override
//    public void onSensorChanged(SensorEvent event) {
//        float valor=event.values[1]; // eix Y
//        if(!hihaValorInicial){
//            valorInicial=valor;
//            hihaValorInicial=true;
//        }
//        girNau=(int)(valor-valorInicial)/3;
//    }
    public void onSensorChanged(SensorEvent event) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (sharedPreferences.getString(getResources().getString(R.string.pa4_key), "0").equals("0")){
            float x=event.values[0],y=event.values[1],z=event.values[2];
            if(!hihaValorInicial){
                initX=x;initY=y;initZ=z;
                hihaValorInicial=true;
            }
            girNau=(int)(initY>0?y-initY:initY-y);
            acceleracioNau=(int)(initZ-z);
        }
        if (sharedPreferences.getString(getResources().getString(R.string.pa4_key), "0").equals("0")){
            float y=event.values[1]; // eix Y
            if(!hihaValorInicial){
                initY=y;
                hihaValorInicial=true;
            }
            girNau=(int)(y-initY)/3;
        }
    }

    class ThreadJoc extends Thread {
        private boolean pausa, corrent;

        public synchronized void pausar() {
            mSensorManager.unregisterListener(VistaJoc.this);
            pausa=true;
        }

        public synchronized void reanudar() {
            pausa=false;
            notify();
            mSensorManager.registerListener(VistaJoc.this, mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0), SensorManager.SENSOR_DELAY_GAME);
        }

        public synchronized void aturar() {
            corrent=false;
            if(pausa) reanudar();
        }
        @Override
        public void run() {
            corrent=true;
            while (corrent) {
                actualitzaFisica();
                synchronized (this) {
                    while(pausa){
                        try {
                            wait();
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }
    }
}
