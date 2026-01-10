package org.example;

import java.util.Scanner;

import org.example.amoeba.db.DBConnectInit;
import org.example.amoeba.db.GameSL;
import org.example.amoeba.db.MentettJatekokMenu;
import org.example.amoeba.meccs.Meccs;
import org.example.amoeba.tabella.Stats;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        DBConnectInit.init();
        GameSL gameSL = new GameSL();


        int option = -1;

        while (true) {

            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println();
            System.out.println("         AMOEBA JATEK         ");
            System.out.println();
            System.out.println("============ MENU ============");
            System.out.println("| 1. Mentett jatek betoltese |");
            System.out.println("| 2. Uj Jatek                |");
            System.out.println("| 3. Tabella                 |");
            System.out.println("| 4. Kilepes                 |");
            System.out.println("==============================");
            System.out.print("Opcio: ");
            String input = sc.nextLine().trim();

            if (!input.matches("\\d+")) {
                System.out.println("Hiba: csak számot adj meg!");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }


            option = Integer.parseInt(input);
            switch (option) {

                case 1:
                    MentettJatekokMenu.menu();
                    break;
                case 2:
                    System.out.println("Uj Jatek");
                    Meccs meccs = new Meccs(gameSL);
                    meccs.start();
                    break;
                case 3:
                    System.out.println("**********Tabella**********");
                    Stats.tabellaMenu();
                    break;
                case 4:
                    System.out.println("Viszlat");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Nincs ilyen opcio, a menubol valassz!");
                    break;
            }
        }

    }

}