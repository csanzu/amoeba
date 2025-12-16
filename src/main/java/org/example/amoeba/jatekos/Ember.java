package org.example.amoeba.jatekos;

import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.tabla.Pozicio;


public class Ember implements Jatekos{

    private final String nev;
    private final JatekosJel jel;

    public Ember(String nev, JatekosJel jel) {
        this.nev = nev;
        this.jel = jel;
    }

    @Override
    public JatekosJel jel() {
        return jel;
    }

    @Override
    public String getNev() {
        return nev;
    }

    @Override
    public Pozicio lep(Tabla tabla) {
        return null;
    }
}
