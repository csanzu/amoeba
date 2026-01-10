package org.example.amoeba.meccs;

import java.util.Map;

import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;
import org.example.amoeba.vos.TablaMeret;

public class JatekAllapot {

    private final TablaMeret meret;
    private final Map<Pozicio, JatekosJel> lepesek;
    private final String emberNev;
    private final JatekosJel aktualisJel;

    public JatekAllapot(
            TablaMeret meret,
            Map<Pozicio, JatekosJel> lepesek,
            String emberNev,
            JatekosJel aktualisJel
    ) {
        this.meret = meret;
        this.lepesek = lepesek;
        this.emberNev = emberNev;
        this.aktualisJel = aktualisJel;
    }

    public TablaMeret getMeret() {
        return meret;
    }

    public Map<Pozicio, JatekosJel> getLepesek() {
        return lepesek;
    }

    public String getEmberNev() {
        return emberNev;
    }

    public JatekosJel getAktualisJel() {
        return aktualisJel;
    }
}
