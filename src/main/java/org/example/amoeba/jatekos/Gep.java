package org.example.amoeba.jatekos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.example.amoeba.meccs.Lepes;
import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Gep implements Jatekos {

    private static final Logger log = LoggerFactory.getLogger(Gep.class);


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
            if (lepes.ervenyes(tabla)) {
                lehetosegek.add(p);
            }
        }

        if (lehetosegek.isEmpty()) {                    // még nincs szomszédos
            lehetosegek = tabla.getSzabadPoziciok();
            log.debug("Gép még nincs szomszédos saját jelhez illeszthető mező, választ a teljes szabad mezők közül");
        }

        int index = rand.nextInt(lehetosegek.size());
        Pozicio valasztott = lehetosegek.get(index);
        log.info("Gép lépése: ({}, {})", valasztott.getSor(), valasztott.getOszlop());
        return valasztott;
    }
}
