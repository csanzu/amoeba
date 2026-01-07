package org.example.amoeba.tabella;

import org.example.amoeba.DB.StatsSL;

//import java.util.List;
import java.util.Scanner;

public class Stats {

    public static void TabellaMenu() {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n=== STATISZTIKAK ===");
        System.out.println("1. Ranglista");
        System.out.println("2. Tablameret statisztika");
        System.out.println("3. Stats DB torles");
        System.out.println("4. Vissza");
        System.out.print("Opcio: ");

        String op = sc.nextLine().trim();

        switch (op) {
            case "1" -> {
                Tabella.kiir(
                        StatsSL.ranglista(),
                        new String[]{"#", "Jatekos", "Gyozelmek"}
                );
            }
            case "2" -> {
                Tabella.kiir(
                        StatsSL.tablaMeretStatisztika(),
                        new String[]{"Tablameret", "Meccsek", "Ember", "Gep"}
                );
            }
            case "3" -> StatsTorlesMenu.megjelenit();
            case "4" -> {
                return;
            }
            default -> System.out.println("Hibas Opcio!");
        }

    }
}
