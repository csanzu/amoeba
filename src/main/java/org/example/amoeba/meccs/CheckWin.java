package org.example.amoeba.meccs;

import org.example.amoeba.vos.Pozicio;
import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;

public class CheckWin {

    private final Tabla tabla;
    private final int nyeresiSzam;

    public CheckWin(Tabla tabla, int nyeresiSzam) {
        this.tabla = tabla;
        this.nyeresiSzam = nyeresiSzam;
    }

    // Ellenőrzi a győzelmet, null ha senki
    public JatekosJel ellenoriz() {
        int magassag = tabla.getMeret().getMagassag();
        int szelesseg = tabla.getMeret().getSzelesseg();

        for (int sor = 1; sor <= magassag; sor++) {
            for (int oszlop = 1; oszlop <= szelesseg; oszlop++) {
                Pozicio p = new Pozicio(sor, oszlop);
                JatekosJel jel = tabla.getPoz(p);
                if (jel == null) continue;

                if (ellenorizIrany(p, 1, 0, jel)) return jel; // vízszint
                if (ellenorizIrany(p, 0, 1, jel)) return jel; // függőleges
                if (ellenorizIrany(p, 1, 1, jel)) return jel; // átló \
                if (ellenorizIrany(p, 1, -1, jel)) return jel; // átló /
            }
        }
        return null; // ha nincs győztes
    }

    // Ellenőrzi adott irányt
    private boolean ellenorizIrany(Pozicio start, int dx, int dy, JatekosJel jel) {
        int count = 0;
        int sor = start.getSor();
        int oszlop = start.getOszlop();

        while (true) {
            if (sor < 1 || oszlop < 1 || sor > tabla.getMeret().getMagassag() || oszlop > tabla.getMeret().getSzelesseg())
                break;
            JatekosJel cellaJel = tabla.getPoz(new Pozicio(sor, oszlop));
            if (cellaJel != jel) break;
            count++;
            if (count >= nyeresiSzam) return true;

            sor += dx;
            oszlop += dy;
        }
        return false;
    }

    // check döntetlen (nincs üres mező)
    public boolean dontetlen() {
        return tabla.getSzabadPoziciok().isEmpty();
    }
}