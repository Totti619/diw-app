package com.antonio.applicacio;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

/**
 * Created by Master on 05/11/2017.
 */

public class PreferenciesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencies);

        final EditTextPreference fragmentos = (EditTextPreference)findPreference(getResources().getString(R.string.pa3_key));

        fragmentos.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        int valor;
                        try{
                            valor=Integer.parseInt((String)newValue);
                        } catch(Exception e) {
                            Toast.makeText(getActivity(), "Ha de ser un numero", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        if(valor>=0 && valor<=9){
                            fragmentos.setSummary("En cuantos trozos se divide un asteroide("+valor+")");
                            return true;
                        } else {
                            Toast.makeText(getActivity(), "Máximo de fragmentos 9", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }
        );
    }
}
