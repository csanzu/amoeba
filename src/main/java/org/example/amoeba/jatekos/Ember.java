package org.example.amoeba.jatekos;

import org.example.amoeba.meccs.Lepes;
import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;

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
    public JatekosJel getJel() {
        return jel;
    }

    @Override
    public String getNev() {
        return nev;
    }

    @Override
    public Pozicio lep(Tabla tabla) {
        while (true) {
            try {
                System.out.print("Adj meg egy lepest (pl. 1 A) vagy M - mentes vagy Q - kilepes: ");
                String input = sc.nextLine().trim().toUpperCase();

                if (input.equals("Q")) {
                    System.out.println("Kilepes..");
                    System.exit(0);
                }

                if (input.equals("M")) {
                    System.out.println("Mentes..");
                    return null;
                }

                String[] parts = input.split("\\s+");
                if (parts.length != 2) {
                    System.out.println("Formatum: sor oszlop (pl. 3 B)");
                    continue;
                }

                int sor = Integer.parseInt(parts[0]);
                char oszlopBetu = parts[1].charAt(0);
                int oszlop = oszlopBetu - 'A' + 1;
                Pozicio poz = new Pozicio(sor, oszlop);

                Lepes lepes = new Lepes(poz, this.getJel());

                if (!lepes.ervenyes(tabla)) {
                    System.out.println("Hibas lepes, ures/szomszedos/tablan beluli.");
                    continue;
                }

                return poz;

            } catch (NumberFormatException e) {
                System.out.println("Sor szam legyen.");
            } catch (Exception e) {
                System.out.println("Input error");
            }
        }
    }
}
