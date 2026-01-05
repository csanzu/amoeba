package org.example.amoeba.jatekos;

import org.example.amoeba.meccs.Lepes;
import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gep implements Jatekos {
    private final JatekosJel jel = JatekosJel.O;
    private final String nev = "Gép";
    private final Random rand = new Random();


    @Override
    public JatekosJel getJel() {
        return jel;
    }

    @Override
    public String getNev() {
        return "Gép";
    }

    /**
     * @Override public Pozicio lep(Tabla tabla) {
     * return null;
     * }
     **/
    @Override
    public Pozicio lep(Tabla tabla) {
        List<Pozicio> lehetosegek = new ArrayList<>();
        for (Pozicio p : tabla.getSzabadPoziciok()) {
            Lepes lepes = new Lepes(p, this.getJel());
            if (lepes.ervenyes(tabla)) lehetosegek.add(p);
        }

        if (lehetosegek.isEmpty()) {
            // még nincs szomszédos mező
            lehetosegek = tabla.getSzabadPoziciok();
        }

        int index = rand.nextInt(lehetosegek.size());
        Pozicio valasztott = lehetosegek.get(index);
        return valasztott;
    }
}
