package org.example;

import org.example.amoeba.DB.*;
import org.example.amoeba.meccs.Meccs;
import org.example.amoeba.tabella.Stats;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int option = -1;

        while(true){

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

                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }


            option = Integer.parseInt(input);
            switch(option){

                case 1:
                    System.out.println("Mentett jatek betoltese");
                    GameSL.GameLoad();
                    break;
                case 2:
                    System.out.println("Uj Jatek");
                    Meccs meccs = new Meccs();
                    meccs.start();
                    break;
                case 3:
                    System.out.println("**********Tabella**********");
                    Stats.TabellaMenu();
                    break;
                case 4:
                    System.out.println("Viszlat");
                    System.exit(0);
                default:
                    System.out.println("Nincs ilyen opcio, a menubol valassz!");
                    break;
            }
        }

    }
}