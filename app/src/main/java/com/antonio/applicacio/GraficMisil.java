package com.antonio.applicacio;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by Antonio Ortiz on 01/02/2018.
 */

public class GraficMisil extends Grafic {
    private boolean misilActiu=true;

    public GraficMisil(View view, Drawable drawable) {
        super(view, drawable);
    }

    public boolean isMisilActiu() {
        return misilActiu;
    }

    public void setMisilActiu(boolean misilActiu) {
        this.misilActiu = misilActiu;
    }

    @Override
    public boolean verificaColisio(Grafic g) {
        if(super.verificaColisio(g))
            setMisilActiu(false);
        return super.verificaColisio(g);
    }
}
