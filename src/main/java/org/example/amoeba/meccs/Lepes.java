package org.example.amoeba.meccs;

import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lepes {

    private static final Logger log = LoggerFactory.getLogger(Lepes.class);

    private final Pozicio pozicio;
    private final JatekosJel jel;

    public Lepes(Pozicio pozicio, JatekosJel jel) {
        this.pozicio = pozicio;
        this.jel = jel;
        //log.debug("Új lépés létrehozva: {} jel, pozíció ({},{})", jel, pozicio.getSor(), pozicio.getOszlop());
    }

    public Pozicio getPozicio() {
        return pozicio;
    }

    public JatekosJel getJel() {
        return jel;
    }

    // Validálás
    public boolean ervenyes(Tabla tabla) {
        if (!tabla.isBelul(pozicio)) {
            log.warn("Érvénytelen lépés: kívül esik a táblán ({},{})", pozicio.getSor(), pozicio.getOszlop());
            return false;  // táblán kívüli
        }
        if (!tabla.isUres(pozicio)) {
            log.warn("Érvénytelen lépés: mező foglalt ({},{})", pozicio.getSor(), pozicio.getOszlop());
            return false;   // foglalt
        }
        if (!tabla.vanSzomszedosJel(pozicio, jel)) {
            log.warn("Érvénytelen lépés: nincs szomszédos saját jel ({},{})", pozicio.getSor(), pozicio.getOszlop());
            return false; // szomszédos saját jel
        }
        log.debug("Érvényes lépés: {} jel, pozíció ({},{})", jel, pozicio.getSor(), pozicio.getOszlop());
        return true;
    }

}
