package org.example.amoeba.jatekos;

import java.util.Scanner;

import org.example.amoeba.meccs.Lepes;
import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ember implements Jatekos {

    private static final Logger log = LoggerFactory.getLogger(Ember.class);

    private final String nev;
    private final JatekosJel jel;
    private final Scanner sc = new Scanner(System.in);

    public Ember(String nev) {
        this.nev = nev;
        this.jel = JatekosJel.X;
        log.info("Új ember játékos létrehozva: {}", nev);
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
        log.debug("{} játékos lépésének bekérése", nev);

        while (true) {
            try {
                System.out.print("Adj meg egy lepest (pl. 1 A) vagy M - mentes vagy Q - kilepes: ");
                String input = sc.nextLine().trim().toUpperCase();

                if (input.equals("Q")) {
                    log.info("{} játékos kilépett a játékból", nev);
                    System.out.println("Kilepes..");
                    System.exit(0);
                }

                if (input.equals("M")) {
                    log.info("{} játékos mentette a játékot", nev);
                    System.out.println("Mentes..");
                    return null; // a Meccs.start() kezelni fogja a mentést
                }

                String[] parts = input.split("\\s+");
                if (parts.length != 2) {
                    log.warn("Helytelen input formátum: {}", input);
                    System.out.println("Formatum: sor oszlop (pl. 3 B)");
                    continue;
                }

                int sor = Integer.parseInt(parts[0]);
                char oszlopBetu = parts[1].charAt(0);
                int oszlop = oszlopBetu - 'A' + 1;
                Pozicio poz = new Pozicio(sor, oszlop);

                Lepes lepes = new Lepes(poz, this.getJel());

                if (!lepes.ervenyes(tabla)) {
                    log.warn("Érvénytelen lépés próbálva: ({}, {})", sor, oszlop);
                    System.out.println("Hibas lepes, ures/szomszedos/tablan beluli.");
                    continue;
                }

                log.info("{} játékos lépése elfogadva: ({}, {})", nev, sor, oszlop);
                return poz;

            } catch (NumberFormatException e) {
                log.warn("{} játékos nem számot adott meg: {}", nev, e.getMessage());
                System.out.println("Sor szam legyen.");
            } catch (Exception e) {
                log.error("Input hiba {} játékosnál", nev, e);
                System.out.println("Input error");
            }
        }
    }
}
