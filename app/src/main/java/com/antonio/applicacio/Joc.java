package com.antonio.applicacio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Joc extends MainActivity {

    @Override
    protected void onPause() {
        super.onPause();
        vistaJoc.getFil().pausar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vistaJoc.getFil().reanudar();
    }

    @Override
    protected void onDestroy() {
        vistaJoc.getFil().aturar();
        super.onDestroy();
    }

    private VistaJoc vistaJoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);
        vistaJoc=(VistaJoc)findViewById(R.id.VistaJoc);
        vistaJoc.setPare(this);
    }
}
