package com.antonio.applicacio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by Antonio Ortiz on 08/11/2017.
 */

public class AdaptadorPuntuacions extends RecyclerView.Adapter<AdaptadorPuntuacions.ViewHolder>{

    protected View.OnClickListener onClickListener;

    // Un adaptador es un mecanisme esàndar en Android que ens permet crear una serie de vistes que
    // han de ser mostrades dins un contenidor. Amb RecyclerView s'ha d'heretar de la classe
    // RecyclerView.Adapter per crear l'adaptador.

    private LayoutInflater inflador; // Crea layouts a partir des XML
    private Vector<String> llista; // Llista de puntuacions

    // En el constructor s'inicialitza el conjunt de dades a mostrar
    public AdaptadorPuntuacions(Context context, Vector<String> llista) {
        // Un inflator permetrà posteriorment crear una vista a partir del seu XML
        inflador=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.llista=llista;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    // Aquesta classe conté les vistes que volem modificar d'un element. Aquesta classe s'utilitza
    // per evitar haver de crear les vistes de cada element des de zero. Utilitzarà un objecte
    // d'aquesta classe amb les tres vistes ja creades però sense personalitzar, de tal manera que
    // emprarà el mateix objecte per a tots els elements i simplement ho personalitzarà segons la
    // posició. Aquesta forma de treballar millora el rendiment de l'aplicació.
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titol, subtitol;
        public ImageView icono;

        public ViewHolder(View itemView){
            super(itemView);
            titol=(TextView)itemView.findViewById(R.id.titol);
            subtitol=(TextView)itemView.findViewById(R.id.subtitol);
            icono=(ImageView)itemView.findViewById(R.id.icono);
        }
    }

    // Aqueste metode retorna una vista d'un element sense personalitzar. Es crida un número de
    // vegades segons les vistes que hi caben en la pantalla ( mès o manco ). Una vegada el sistema
    // ja te les vistes necessàries per anar mostrant els elements ja no en creen més i reutilitza
    // les existents. Podriem definir diferentes vistes per a difernets tipus d'elements utilitzant
    // el paràmetre viewType.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // El metode inflate() crea (infla) la vista des de xml. El segon parametre indica el layout
        // pare que contindra a la vista que s'ha de crear. És necessari indicar-ho perquè volem que
        // la vista filla s'adapti a la grandària del pare ("match_parent"). El tercer paràmetre
        // permet indicar si volem que la vista sigui insetada en el pare. En el nostre cas serà
        // false perquè ho farà el mateix RecyclerView
        View v= inflador.inflate(R.layout.element_puntuacio, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    // Aquest mètode personalitza les dades d'un element de tipus ViewHolder segons la seva posició.
    // A partir del ViewHolder personalitzat el sistema s'encarrega de crear la vista definitica que
    // serà insertada en el RecyclerView.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titol.setText(llista.get(position));
        switch (Math.round((float)Math.random()*3)) {
            case 0:
                holder.icono.setImageResource(R.drawable.asteroide1);
                break;
            case 1:
                holder.icono.setImageResource(R.drawable.asteroide2);
                break;
            default:
                holder.icono.setImageResource(R.drawable.asteroide3);
                break;
        }
    }

    // Indica el número total d'elemtns a visualitzar
    @Override
    public int getItemCount() {
        return llista.size();
    }
}
