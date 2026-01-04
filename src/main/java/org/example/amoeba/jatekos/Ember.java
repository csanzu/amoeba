package org.example.amoeba.jatekos;

import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.tabla.Pozicio;

import java.util.Scanner;


public class Ember implements Jatekos{

    private final String nev;
    private final JatekosJel jel;
    private final Scanner sc = new Scanner(System.in);

    public Ember(String nev) {
        this.nev = nev;
        this.jel = JatekosJel.X;
    }

    @Override
    public JatekosJel jel() {
        return jel;
    }

    @Override
    public String getNev() {
        return nev;
    }

    @Override
    public Pozicio lep(Tabla tabla) {
        return null;
    }
}
