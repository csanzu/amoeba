package org.example.amoeba.jatekos;

import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.tabla.Pozicio;

import java.util.List;
import java.util.Random;

public class Gep implements Jatekos {
    private final JatekosJel jel = JatekosJel.O;
    private final String nev = "Gép";
    private final Random rand = new Random();


    @Override
    public JatekosJel jel() {
        return jel;
    }
    @Override
    public String getNev() {
        return "Gép";
    }

    @Override
    public Pozicio lep(Tabla tabla) {
        return null;
    }

    public Pozicio lerakGep(Tabla tabla) {
        List<Pozicio> szabad = tabla.getSzabadPoziciok();
        if (szabad.isEmpty()) {
            return null;
        }
        return szabad.get(rand.nextInt(szabad.size()));
    }
}
