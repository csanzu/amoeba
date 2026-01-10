package org.example.amoeba.meccs;

import java.util.Scanner;

import org.example.amoeba.db.GameSL;
import org.example.amoeba.db.StatsSL;
import org.example.amoeba.jatekos.Ember;
import org.example.amoeba.jatekos.Gep;
import org.example.amoeba.jatekos.Jatekos;
import org.example.amoeba.tabla.Rajzolo;
import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;
import org.example.amoeba.vos.TablaMeret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Meccs {

    private static final Logger log = LoggerFactory.getLogger(Meccs.class);

    Scanner sc = new Scanner(System.in);

    private final GameSL gameSL;
    private Tabla tabla;
    private Jatekos ember;
    private Jatekos gep;
    private Jatekos aktualis;
    private TablaMeret meret;

    Rajzolo rajzolo = new Rajzolo();

    public Meccs() {
        gameSL = new GameSL();
    }

    void setScanner(Scanner sc) {
        this.sc = sc;
    }

    public Meccs(GameSL gameSL) {
        this.gameSL = gameSL;
    }

    public Meccs(JatekAllapot allapot, GameSL gameSL) {

        this.gameSL = gameSL;
        betolt(allapot);
    }

    //TÁBLA MÉRET BEOLVASÁS
    private int bekerMeret(String uzenet) {
        int szam;
        while (true) {
            System.out.print(uzenet);
            String input = sc.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println("Hiba: csak számot adj meg!");
                continue;
            }
            szam = Integer.parseInt(input);
            if (szam < 4 || szam > 25) {
                System.out.println("4 es 25 kozotti szamot adj meg!");
                continue;
            }
            return szam;
        }
    }
    //TÁBLAMÉRET BEOLVASÁS VÉGE

    //JÁTÉKOS NÉV BEKÉRÉS
    private String nevBeker() {
    while (true) {
        System.out.print("Add meg a neved: ");
        String nev = sc.nextLine().trim();

        if (nev.isEmpty()) {
            System.out.println("A név nem lehet üres!");
            continue;
        }

        return nev;
    }
}
    //JÁTÉKOS NÉV BEKÉRÉS VÉGE


    //KEZDŐ POZÍCIÓRA x HELYEZÉS
    private Pozicio kozepPozicio() {
        int sor = (meret.getMagassag() + 1) / 2;
        int oszlop = (meret.getSzelesseg() + 1) / 2;
        return new Pozicio(sor, oszlop);
    }
    //KEZDŐRE HELYEZÉS VÉGE

    //NYERÉSI HOSSZ
    private int nyeresiHossz() {
        int max = Math.max(
            meret.getSzelesseg(),
            meret.getMagassag()
        );

    return (max > 4) ? 5 : 4;
}
    //NYERÉSI HOSSZ VÉGE

    //ÁLLAPOT EXPORT
    public JatekAllapot exportAllapot() {
    return new JatekAllapot(
            tabla.getMeret(),
            tabla.getOsszesLepes(),
            ember.getNev(),
            aktualis.getJel()
    );
}
    //ÁLLAPOT EXPORT VÉGE

    //JÁTÉK BETÖLTÉS
    private void betolt(JatekAllapot allapot) {
        log.info("Mentett játék betöltése: játékos={}, aktuális jel={}", allapot.getEmberNev(), allapot.getAktualisJel());
        this.meret = allapot.getMeret();
        this.tabla = new Tabla(meret);

        for (var e : allapot.getLepesek().entrySet()) {
            tabla.lerak(e.getKey(), e.getValue());
        }

        this.ember = new Ember(allapot.getEmberNev());
        this.gep = new Gep();

        this.aktualis =
            allapot.getAktualisJel() == ember.getJel()
                    ? ember
                    : gep;
}
    //JÁTÉK BETÖLTÉS VÉGE

    //JÁTÉK START
    public void start() {

        if (tabla == null) {
            ujJatek();
        }

        int nyeresiSzam = nyeresiHossz();
        CheckWin check = new CheckWin(tabla, nyeresiSzam);

        while (true) {
            rajzolo.rajzolo(tabla);

            log.info("Következő lépő: {} ({})", aktualis.getNev(), aktualis.getJel());

            System.out.println("A kovetkezo lep: "
                + aktualis.getNev()
                + " (" + aktualis.getJel() + ")");

            Pozicio lepes = aktualis.lep(tabla);
            if (lepes == null) {
                log.info("Játék mentése folyamatban...");
                gameSL.save(exportAllapot());
                return;
            }

            tabla.lerak(lepes, aktualis.getJel());
            log.debug("Lépés megtörtént: {} ({},{})", aktualis.getJel(), lepes.getSor(), lepes.getOszlop());

            JatekosJel nyertes = check.ellenoriz();
            if (nyertes != null) {
                rajzolo.rajzolo(tabla);
                String nev = nyertes == JatekosJel.X ? ember.getNev() : gep.getNev();
                System.out.println("A gyoztes: "
                    + (nyertes == JatekosJel.X ? ember.getNev() : gep.getNev()));
                log.info("Játék vége, győztes: {}", nev);

                StatsSL.ment(
                        nyertes == JatekosJel.X ? ember.getNev() : gep.getNev(),
                        meret.getSzelesseg(),
                        meret.getMagassag(),
                        nyertes == JatekosJel.X ? ember.getNev() : gep.getNev(),
                        tabla.getOsszesLepes().size()
                );

                break;
            }

            if (check.dontetlen()) {
                rajzolo.rajzolo(tabla);
                System.out.println("Dontetlen!");
                log.info("Játék vége: döntetlen");
                break;
            }

            aktualis = (aktualis == ember) ? gep : ember;
        }
    }
    //JÁTÉK START VÉGE

    private void ujJatek() {
        log.info("Új játék kezdése");

        String nev = nevBeker();
        log.info("Új ember játékos: {}", nev);

        this.ember = new Ember(nev);
        this.gep = new Gep();

        while (true) {
            int sz = bekerMeret("Szelesseg (4-25): ");
            int m = bekerMeret("Magassag (4-25): ");

            try {
                this.meret = new TablaMeret(sz, m);
                log.info("Tábla létrehozva: {}x{}", sz, m);
                break;
            } catch (IllegalArgumentException e) {
                log.warn("Érvénytelen tábla méret: {}x{}", sz, m);
                System.out.println(e.getMessage());
            }
        }

        this.tabla = new Tabla(meret);

        Pozicio kezdo = kozepPozicio();
        tabla.lerak(kezdo, JatekosJel.X);
        log.info("Kezdő X jel pozíció: ({},{})", kezdo.getSor(), kezdo.getOszlop());

        this.aktualis = gep;
        log.info("Kezdő játékos: Gép");
    }

    public void setTabla(Tabla tabla) {
        this.tabla = tabla;
    }

    public void setEmber(Jatekos ember) {
        this.ember = ember;
    }

    public void setGep(Jatekos gep) {
        this.gep = gep;
    }

    public void setAktualis(Jatekos aktualis) {
        this.aktualis = aktualis;
    }

    public void setMeret(TablaMeret meret) {
        this.meret = meret;
    }

    public GameSL getGameSL() {
        return gameSL;
    }

    public Tabla getTabla() {
        return tabla;
    }

    public Jatekos getEmber() {
        return ember;
    }

    public Jatekos getGep() {
        return gep;
    }

    public Jatekos getAktualis() {
        return aktualis;
    }

    public TablaMeret getMeret() {
        return meret;
    }
}
