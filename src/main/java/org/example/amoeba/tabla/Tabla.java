package org.example.amoeba.tabla;

import org.example.amoeba.vos.TablaMeret;
import org.example.amoeba.vos.JatekosJel;

import java.util.ArrayList;
import java.util.List;

public class Tabla {

    private final TablaMeret meret;
    private final JatekosJel [][] cella;


    public Tabla(TablaMeret meret) {
        this.meret = meret;
        this.cella = new JatekosJel[meret.getMagassag()+1][meret.getSzelesseg()+1];
    }

    public boolean isUres(Pozicio pozicio) {
        return cella[pozicio.getSor()][pozicio.getOszlop()] == null;
    }

    public boolean isBelul(Pozicio pozicio) {
        return pozicio.getSor() >=1 && pozicio.getSor() <= meret.getMagassag() && pozicio.getOszlop() >=1 && pozicio.getOszlop() <= meret.getSzelesseg();
    }

    public JatekosJel getPoz(Pozicio pozicio) {
        return cella[pozicio.getSor()][pozicio.getOszlop()];
    }

    public List<Pozicio> getSzabadPoziciok() {
        List<Pozicio> szabad = new ArrayList<>();
        for (int s=1; s<meret.getMagassag(); s++) {
            for (int o=1; o<meret.getSzelesseg(); o++) {
                if (cella[s][o] == null) {
                    szabad.add(new Pozicio(s,o));
                }
            }
        }
        return szabad;
    }
    public void Lerak(Pozicio pozicio, JatekosJel jel) {
        if (!isUres(pozicio)) {
            throw new IllegalArgumentException("A hely mar foglalt");
        }
        cella[pozicio.getSor()][pozicio.getOszlop()] = jel;
    }
    public TablaMeret getMeret() {
        return meret;
    }
}
