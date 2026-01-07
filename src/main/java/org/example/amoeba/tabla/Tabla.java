package org.example.amoeba.tabla;

import org.example.amoeba.vos.Pozicio;
import org.example.amoeba.vos.TablaMeret;
import org.example.amoeba.vos.JatekosJel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tabla {

    private final TablaMeret meret;
    private final JatekosJel [][] cella;

    public Tabla(TablaMeret meret) {
        this.meret = meret;
        this.cella = new JatekosJel[meret.getMagassag()][meret.getSzelesseg()];
    }
    public JatekosJel getPoz(Pozicio pozicio) {
        return cella[pozicio.getSor()-1][pozicio.getOszlop()-1];
    }
    public List<Pozicio> getSzabadPoziciok() {
        List<Pozicio> szabad = new ArrayList<>();
        for (int s=0; s<meret.getMagassag(); s++) {
            for (int o=0; o<meret.getSzelesseg(); o++) {
                if (cella[s][o] == null) {
                    szabad.add(new Pozicio(s+1,o+1));
                }
            }
        }
        return szabad;
    }
    public TablaMeret getMeret() {
        return meret;
    }

    public boolean isUres(Pozicio pozicio) {
        return cella[pozicio.getSor()-1][pozicio.getOszlop()-1] == null;
    }
    public boolean isBelul(Pozicio pozicio) {
        return pozicio.getSor() >=1 && pozicio.getSor() <= meret.getMagassag() && pozicio.getOszlop() >=1 && pozicio.getOszlop() <= meret.getSzelesseg();
    }
    public boolean vanSzomszedosJel(Pozicio poz, JatekosJel jel) {
        int s = poz.getSor() - 1;       // 0-index
        int o = poz.getOszlop() - 1;

        // minden szomszédos mező: fel, le, bal, jobb, átlók
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int ujSor = s + dx[i];
            int ujOszlop = o + dy[i];

            // ellenőrzés, hogy a mező a táblán belül van-e
            if (ujSor >= 0 && ujSor < meret.getMagassag()
                    && ujOszlop >= 0 && ujOszlop < meret.getSzelesseg()) {

                if (cella[ujSor][ujOszlop] != null) {
                    return true; // bármilyen jel
                }
            }
        }

        return false; // nincs szomszédos jel
    }
    public void Lerak(Pozicio pozicio, JatekosJel jel) {
        if (!isBelul(pozicio)) {
            throw new IllegalArgumentException("Pozíció táblán kívül");
        }
        if (!isUres(pozicio)) {
            throw new IllegalArgumentException("A hely mar foglalt");
        }
        cella[pozicio.getSor()-1][pozicio.getOszlop()-1] = jel;
    }
    public Map<Pozicio, JatekosJel> getOsszesLepes() {
        Map<Pozicio, JatekosJel> map = new HashMap<>();
        for (int s = 1; s <= meret.getMagassag(); s++) {
            for (int o = 1; o <= meret.getSzelesseg(); o++) {
                if (cella[s-1][o-1] != null) {
                    map.put(new Pozicio(s, o), cella[s-1][o-1]);
                }
            }
        }
        return map;
    }

}
