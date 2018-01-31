package com.antonio.applicacio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Antonio Ortiz on 08/11/2017.
 */

public class Puntuacions extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorPuntuacions adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Assigna layout a l'activitat
        setContentView(R.layout.puntuacions);

        // Configura el recyclerView
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        // Especifica el tipus de format de visualitzacio del RecyclerView
        // i l'assigna.
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // Crea i assigna adaptador del RecyclerView
        adaptador= new AdaptadorPuntuacions(this, MainActivity.magatzemPuntuacions.llistaPuntuacions(10));
        recyclerView.setAdapter(adaptador);

        adaptador.setOnItemClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Metode que indica la posicio d'una vista dins l'adaptador
                int pos=recyclerView.getChildAdapterPosition(v);
                String s= MainActivity.magatzemPuntuacions.llistaPuntuacions(10).get(pos);
                Toast.makeText(Puntuacions.this,"Selecció: "+pos+" - "+s, Toast.LENGTH_LONG).show();
            }
        });
    }
}
