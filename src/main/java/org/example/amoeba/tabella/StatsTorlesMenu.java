package org.example.amoeba.tabella;

import java.util.Scanner;

import org.example.amoeba.db.StatsSL;

public class StatsTorlesMenu {

    public static void megjelenit() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== STATISZTIKÁK TÖRLÉSE ===");
            System.out.println("1. Összes stat törlése");
            System.out.println("2. Egy játékos statjainak törlése");
            System.out.println("3. Vissza");
            System.out.print("Opció: ");

            String input = sc.nextLine().trim();

            switch (input) {
                case "1" -> {
                    if (megerosites(sc)) {
                        StatsSL.torolMindent();
                        System.out.println("Minden stat törölve ✔");
                    }
                }

                case "2" -> {
                    System.out.print("Játékos neve: ");
                    String nev = sc.nextLine().trim();
                    if (nev.isEmpty()) {
                        System.out.println("Név nem lehet üres!");
                        break;
                    }
                    if (megerosites(sc)) {
                        StatsSL.torolJatekos(nev);
                        System.out.println("Statisztikák törölve: " + nev);
                    }
                }

                case "3" -> {
                    return;
                }

                default -> System.out.println("Érvénytelen opció!");
            }
        }
    }

    private static boolean megerosites(Scanner sc) {
        System.out.print("Biztos vagy benne? (igen/nem): ");
        return sc.nextLine().trim().equalsIgnoreCase("igen");
    }
}
