package org.example.amoeba.meccs;

import org.example.amoeba.DB.*;
import org.example.amoeba.jatekos.*;
import org.example.amoeba.tabla.*;
import org.example.amoeba.vos.*;

import java.util.Scanner;

public class Meccs {

    Scanner sc = new Scanner(System.in);

    private final GameSL gameSL;
    private Tabla tabla;
    private Jatekos ember;
    private Jatekos gep;
    private Jatekos aktualis;
    private TablaMeret meret;

    Rajzolo rajzolo = new Rajzolo();

    public Meccs(GameSL gameSL) {
        this.gameSL = gameSL;
    }

    public Meccs(JatekAllapot allapot, GameSL gameSL) {

        this.gameSL = gameSL;
        betolt(allapot);
    }


//TÁBLA MÉRET BEOLVASÁS
    private int bekerMeret(String uzenet){
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
    this.meret = allapot.getMeret();
    this.tabla = new Tabla(meret);

    for (var e : allapot.getLepesek().entrySet()) {
        tabla.Lerak(e.getKey(), e.getValue());
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

            System.out.println("A kovetkezo lep: "
                + aktualis.getNev()
                + " (" + aktualis.getJel() + ")");

            Pozicio lepes = aktualis.lep(tabla);
            if (lepes == null) {
                gameSL.save(exportAllapot());
                return;
            }

            tabla.Lerak(lepes, aktualis.getJel());

            JatekosJel nyertes = check.ellenoriz();
            if (nyertes != null) {
                rajzolo.rajzolo(tabla);
                System.out.println("A gyoztes: "
                    + (nyertes == JatekosJel.X ? ember.getNev() : gep.getNev()));

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
                break;
            }

            aktualis = (aktualis == ember) ? gep : ember;
        }
    }
//JÁTÉK START VÉGE

    private void ujJatek() {

        String nev = nevBeker();
        this.ember = new Ember(nev);
        this.gep = new Gep();

        while (true) {
            int sz = bekerMeret("Szelesseg (4-25): ");
            int m = bekerMeret("Magassag (4-25): ");

            try {
                this.meret = new TablaMeret(sz, m);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        this.tabla = new Tabla(meret);

        Pozicio kezdo = kozepPozicio();
        tabla.Lerak(kezdo, JatekosJel.X);

        this.aktualis = gep;
    }


}
