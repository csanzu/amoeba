package org.example.amoeba.meccs;

import org.example.amoeba.vos.Pozicio;
import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;

public class Lepes {

    private final Pozicio pozicio;
    private final JatekosJel jel;

    public Lepes(Pozicio pozicio, JatekosJel jel) {
        this.pozicio = pozicio;
        this.jel = jel;
    }
    public Pozicio getPozicio() {
        return pozicio;
    }
    public JatekosJel getJel() {
        return jel;
    }

    // Validálás
    public boolean ervenyes(Tabla tabla) {
        if (!tabla.isBelul(pozicio)) return false;  // táblán kívüli
        if (!tabla.isUres(pozicio)) return false;   // foglalt
        if (!tabla.vanSzomszedosJel(pozicio, jel)) return false; // szomszédos saját jel
        return true;
    }

}
