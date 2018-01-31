package com.antonio.applicacio;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Antonio Ortiz on 31/01/2018.
 */

public class MagatzemPuntuacionsXML_SAX implements MagatzemPuntuacions {

    // nom del fitxer on es quardaran les dades
    // data/data/com.example.aplicacio1/files/
    private static String FITXER="puntuacions.xml";
    private Context context;
    // per guardar la informacio llegida del fitxer XML
    private LlistaPuntuacions llista;
    // indica si la variable llista ja ha sigut llegida des de fitxer
    private boolean carregadaLlista;

    public MagatzemPuntuacionsXML_SAX(Context context){
        this.context=context;
        llista=new LlistaPuntuacions();
        carregadaLlista=false;
    }

    @Override
    public void guardarPuntuacio(int punts, String nom, long data) {
        try {
            // Comprovem si la variable llista te les dades
            if(!carregadaLlista) {
                // Llegeix dades del fitxer XML
                llista.llegirXML(context.openFileInput(FITXER));
            }
        } catch (FileNotFoundException e) {
            // Si es la primera vegada l'arxiu no existira i es llançar
            // aquesta excepcio, pero no passa res, no relitzem cap accio.
            Log.e("Asteroides", e.getMessage(), e);
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
        // afegeix la nova puntuacio a la llista
        llista.nou(punts,nom,data);
        try {
            // escriu de nou tota la informacio de la llista al fitxer
            llista.escriureXML(context.openFileOutput
                    (FITXER, Context.MODE_PRIVATE));
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int quntitat) {
        try {
            // Comprovem si la variable llista te les dades
            if(!carregadaLlista) {
                // Llegeix dades del fitxer XML
                llista.llegirXML(context.openFileInput(FITXER));
            }
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
        // retorna la llista en el format esperat Vector.
        return llista.aVectorString();
    } // tanca MagatzemPUntuacionsXML_SAX

    private class LlistaPuntuacions {
        // Una altre classe interna
        private class Puntuacio {
            int punts;
            String nom;
            long data;
        }

        // Variable que realment conte les dades XML
        private List<Puntuacio> llistaPuntuacions;

        public LlistaPuntuacions() {
            llistaPuntuacions=new ArrayList<Puntuacio>();
        }

        // afegeix una nova puntuacio a la llista
        public void nou(int punts, String nom, long data) {
            Puntuacio puntuacio=new Puntuacio();
            puntuacio.punts=punts;
            puntuacio.nom=nom;
            puntuacio.data=data;
            llistaPuntuacions.add(puntuacio);
        }

        // extreu la informacio que interessa de la llista i
        // construeix un vector de strings amb la informacio
        public Vector<String> aVectorString() {
            Vector<String> result = new Vector<String>();
            for(Puntuacio puntuacio: llistaPuntuacions) {
                result.add(puntuacio.nom+" "+puntuacio.punts);
            }
            return result;
        }

        public void llegirXML(InputStream entrada) throws Exception {
            SAXParserFactory fabrica= SAXParserFactory.newInstance();
            SAXParser parser= fabrica.newSAXParser();
            XMLReader lector= parser.getXMLReader();
            ManejadorXML manejadorXML=new ManejadorXML();
            lector.setContentHandler(manejadorXML);
            lector.parse(new InputSource(entrada));
            carregadaLlista=true;
        }

        class ManejadorXML extends DefaultHandler {
            // El manejador s'encarrega de generear la llista de puntuacions.
            private StringBuilder cadena;
            private Puntuacio puntuacio;

            @Override
            public void startDocument() throws SAXException {
                llistaPuntuacions=new ArrayList<Puntuacio>();
                cadena=new StringBuilder();
            }

            @Override
            public void startElement(String uri, String nomLocal, String nomQualif, Attributes atr)
                    throws SAXException {
                // Cada vegada que comença un nou element reiniciem la cadena
                cadena.setLength(0);
                // tractar l'etiqueta puntuacio. Les altres descartades
                if(nomLocal.equals("puntuacio")) {
                    // Comenca un nou objecte
                    puntuacio=new Puntuacio();
                    // llegir l'attribut data de l'etiqueta. Ho revem per l'argument
                    puntuacio.data= Long.parseLong(atr.getValue("data"));
                }
            }

            @Override
            // Es crida quan aparaeix un text dins d'una etiqueta
            public void characters(char[] ch, int inici, int lon) throws SAXException {
                // guardem el text dins un string i despres el tractem
                cadena.append(ch,inici,lon);
                // SAX no garanteix que ens passara tot el text en un sol event,
                // si el text es molt extens es realitzaran diferents cridades
                // a aquest metode. Per aixo el text es va acumulant amb append().
            }

            @Override
            // En funcio de l'etiqueta que estiguem acabant realitzarem una tasca
            // diferent. Si es tracta de punts o noms utilitzarem el valor de la
            // variable cadena per actualitzar el valor corresponent de l'objecte.
            // Si es tracta de puntuacio afegim l'objecte a la llista
            public void endElement(String uri, String nomLocal, String nomQualif) throws SAXException {
                if(nomLocal.equals("punts")) {
                    puntuacio.punts=Integer.parseInt(cadena.toString());
                } else if(nomLocal.equals("nom")) {
                    puntuacio.nom=cadena.toString();
                } else if(nomLocal.equals("puntuacio")) {
                    llistaPuntuacions.add(puntuacio);
                }
            }

            @Override
            public void endDocument() throws SAXException {

            }
        }
        public void escriureXML(OutputStream sortida) {
            XmlSerializer serialitzador= Xml.newSerializer();
            try {
                serialitzador.setOutput(sortida, "UTF8");
                serialitzador.startDocument("UTF-8",true);
                serialitzador.startTag("","llista_puntuacions");
                for(LlistaPuntuacions.Puntuacio puntuacio:llistaPuntuacions) {
                    serialitzador.startTag("","puntuacio");
                    serialitzador.attribute("","data",
                            String.valueOf(puntuacio.data));
                    serialitzador.startTag("","nom");
                    serialitzador.text(puntuacio.nom);
                    serialitzador.endTag("","nom");
                    serialitzador.startTag("","punts");
                    serialitzador.text(String.valueOf(puntuacio.punts));
                    serialitzador.endTag("","punts");
                    serialitzador.endTag("","puntuacio");
                }
                serialitzador.endTag("","llista_puntuacions");
                serialitzador.endDocument();
            } catch (Exception e) {
                Log.e("Asteroides", e.getMessage(), e);
            }
        }
    }
}