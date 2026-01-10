package org.example.amoeba.tabla;

import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;

public class Rajzolo {

    public void rajzolo(Tabla tabla) {
        final int magassag = tabla.getMeret().getMagassag();
        final int szelesseg = tabla.getMeret().getSzelesseg();

        System.out.println();
        System.out.println("Aktualis allas:");
        System.out.println();

        //Vízszintes elválasztás
        System.out.print("    ");
        for (int s = 1; s <= szelesseg; s++) {
            System.out.print(" " + (char) ('A' + s - 1));
        }
        System.out.println();
        //Sorok rajzolása
        for (int m = 1; m <= magassag; m++) {

            //sorszám
            System.out.printf("%3d|", m);

            //cellák
            for (int s = 1; s <= szelesseg; s++) {
                JatekosJel jatekosjel = tabla.getPoz(new Pozicio(m, s));
                char ch = '.';
                if (jatekosjel == JatekosJel.X) {
                    ch = 'X';
                }
                if (jatekosjel == JatekosJel.O) {
                    ch = 'O';
                }
                System.out.print(" " + ch);
            }
            System.out.println();
        }
        System.out.println();
    }
}
