package org.example.amoeba.meccs;

import org.example.amoeba.DB.*;
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
        rajzolo.rajzolo(tabla);

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

    public void start() {



    }
}
