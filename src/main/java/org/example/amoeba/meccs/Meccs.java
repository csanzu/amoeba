package org.example.amoeba.meccs;

import org.example.amoeba.jatekos.*;
import org.example.amoeba.tabla.*;
import org.example.amoeba.vos.*;

import java.util.Scanner;

public class Meccs {

    Scanner sc = new Scanner(System.in);

    private Tabla tabla;
    private Jatekos ember;
    private Jatekos gep;
    private Jatekos aktualis;
    private TablaMeret meret;

    Rajzolo rajzolo = new Rajzolo();

    public Meccs(Tabla tabla, Jatekos ember, Jatekos gep, Jatekos aktualis) {
        this.tabla = tabla;
        this.ember = ember;
        this.gep = gep;
        this.aktualis = aktualis;
    }

    public Meccs() {

        String nev = nevBeker();
        this.ember = new Ember(nev);
        this.gep = new Gep();

        while (true) {

            System.out.println();
            System.out.println("     min: 4x4 | max: 25x25");
            System.out.println("Mekkora tablan szeretnel jatszani?: ");

            int szelesseg = bekerMeret("Szelesseg (4-25): ");
            int magassag = bekerMeret("Magassag (4-25): ");

            try {
                this.meret = new TablaMeret(szelesseg, magassag);
            }
            catch (IllegalArgumentException e) {
                System.out.println("HIBA: " + e.getMessage());
                continue;
            }
            break;
        }
        this.tabla = new Tabla(this.meret);
        //Pozicio kezdo = kozepPozicio();
        //tabla.Lerak(kezdo, JatekosJel.X);
        rajzolo.rajzolo(tabla);
        this.aktualis = ember;

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


//JÁTÉK START
    public void start() {
        System.out.println("UGABUGAAAAAAAAAA");

        Pozicio kezdo = kozepPozicio();
        tabla.Lerak(kezdo, JatekosJel.X);
        aktualis = gep;
        int nyeresiSzam = nyeresiHossz();

        CheckWin check = new CheckWin(tabla, nyeresiSzam);

        while (true) {
            rajzolo.rajzolo(tabla);

            System.out.println("A kovetkezo lep: " + aktualis.getNev()
                    + " (" + aktualis.getJel() + ")");

            Pozicio lepes = aktualis.lep(tabla);

            tabla.Lerak(lepes, aktualis.getJel());

            JatekosJel nyertes = check.ellenoriz();
            if (nyertes != null) {
                rajzolo.rajzolo(tabla);
                System.out.println("A gyoztes: " + (nyertes == JatekosJel.X ? ember.getNev() : gep.getNev()));
                break;
            }

            if (check.döntetlen()) {
                rajzolo.rajzolo(tabla);
                System.out.println("Dontetlen!");
                break;
            }

            aktualis = (aktualis == ember) ? gep : ember;
        }
    }
    //JÁTÉK START VÉGE
}
