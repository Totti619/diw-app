package com.antonio.applicacio;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by Antonio Ortiz on 18/01/2018.
 */

public class ServeiMusica extends Service {
    private static final int ID_NOTIFICACIO_CREAR=1;
    MediaPlayer reproductor;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Servei creat", Toast.LENGTH_SHORT).show();
        reproductor= MediaPlayer.create(this, R.raw.audio);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int idArranc) {
        // Creacio de la notificacio
        Notification.Builder notificacio=new Notification.Builder(this)
            .setContentTitle("Creant Servei de Música")//titol que descriu la notificacio
            .setSmallIcon(R.mipmap.ic_launcher) // Icono a visualitzar
            .setContentText("Informacio addicional") // informacio mes detallada
            .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                    android.R.drawable.ic_media_play))
            .setWhen(System.currentTimeMillis()+1000*60*60)
            .setContentInfo("més info")
            .setTicker("Text en barra d'estat");
        // Referencia que permet manejar les notificacions del sistema
        NotificationManager nm;

        // El primer parametre indica un id per identificar aquesta notificacio
        // en el futur, i el segon la notificacio
        PendingIntent intencioPendent= PendingIntent.getActivity(this,0,
                new Intent(this, Joc.class), 0);
        notificacio.setContentIntent(intencioPendent);

        nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        // Igual que (SensorManager)getSystemService(SENSOR_SERVICE;
        // Igual que (LocationManager)getSystemService(LOCATION_SERVICE);
        // Llança la notificacio
        nm.notify(ID_NOTIFICACIO_CREAR, notificacio.build());

        notificacio.setContentIntent(intencioPendent);
        super.onStartCommand(intent, flags, idArranc);
        Toast.makeText(this, "Servei arrancat "+idArranc, Toast.LENGTH_SHORT).show();
        reproductor.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servei aturat",Toast.LENGTH_SHORT).show();
        reproductor.stop();

        // Elimina la notificacio si el servei deixa d'estar actiu.
        NotificationManager nm;
        nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(ID_NOTIFICACIO_CREAR);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
