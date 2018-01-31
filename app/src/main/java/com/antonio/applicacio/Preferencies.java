package com.antonio.applicacio;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Master on 05/11/2017.
 */

public class Preferencies extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenciesFragment()).commit();
    }
}
