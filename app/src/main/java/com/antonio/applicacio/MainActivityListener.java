package com.antonio.applicacio;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Antonio Ortiz on 22/11/2017.
 */

public class MainActivityListener extends Activity implements View.OnClickListener, View.OnLongClickListener {
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.buttonJugar)
            llancarJoc(view);
        if(view.getId()==R.id.buttonAcercaDe)
            llancarAcercaDe(view);
        if(view.getId()==R.id.buttonConfigurar)
            llancarConfiguracio(view);
        if(view.getId()==R.id.buttonSalir)
            super.finish();
    }

    @Override
    public boolean onLongClick(View view) {
        if(view.getId()==R.id.buttonJugar)
            llancarJoc(view);
        if(view.getId()==R.id.buttonAcercaDe)
            llancarAcercaDe(view);
        if(view.getId()==R.id.buttonConfigurar)
            llancarConfiguracio(view);
        if(view.getId()==R.id.buttonSalir)
            llancarPunctuacions(view);
        return false;
    }

    public void llancarJoc(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), Joc.class));
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

    public void llancarPunctuacions(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), Puntuacions.class));
    }

    public void llancarAcercaDe(View view) {
        Intent i = new Intent(view.getContext(), AcercaDe.class);
        view.getContext().startActivity(i);
    }

    public void llancarConfiguracio(View view) {
        view.getContext().startActivity(
                new Intent(view.getContext(), Preferencies.class)
        );
    }

    // Cada vegada que es selecciona el menú es crida el següent mètode
    // per què tracti els esdeveniments capturats.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.config) {
            // indica que l'event ha sigut tractat i que no s'ha de propagar més.
            llancarConfiguracio(null);
            return true;
        }
        if (id==R.id.acercaDe) {
            llancarAcercaDe(null);
            // Indica que l'event ha sigut tractat i que no s'ha de propagar més.
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
