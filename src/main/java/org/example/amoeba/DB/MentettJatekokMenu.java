package org.example.amoeba.DB;


import org.example.amoeba.meccs.JatekAllapot;
import org.example.amoeba.meccs.Meccs;

import java.util.List;
import java.util.Scanner;

public class MentettJatekokMenu {

    public static void menu() {
        Scanner sc = new Scanner(System.in);
        GameSL gameSL = new GameSL();

        while (true) {
            System.out.println("\n===== Mentett jatekok =====");
            System.out.println("1. Mentett jatek betoltese");
            System.out.println("2. Osszes mentes torlese");
            System.out.println("3. Mentesek exportalasa XML-be");
            System.out.println("4. Mentesek importalasa XML-ből");
            System.out.println("0. Vissza");

            System.out.print("Opcio: ");
            String op = sc.nextLine().trim();

            switch (op) {
                case "1":
                    List<SavedGameInfo> saves = gameSL.listSaves();
                    if (saves.isEmpty()) {
                        System.out.println("Nincs mentett jatek!");
                        break;
                    }
                    for (int i = 0; i < saves.size(); i++) {
                        System.out.printf("%d. %s (%s)%n", i + 1, saves.get(i).playerName(), saves.get(i).savedAt());
                    }
                    System.out.print("Valasztas: ");
                    int choice = Integer.parseInt(sc.nextLine());
                    JatekAllapot allapot = gameSL.load(saves.get(choice - 1).id());
                    new Meccs(allapot, gameSL).start();
                    break;
                case "2":
                    System.out.print("Biztos torlod az osszes mentest? (i/n): ");
                    String confirm = sc.nextLine().trim().toLowerCase();

                    if (confirm.equals("i")) {
                        GameSL.deleteAll();
                    } else {
                        System.out.println("Torles megszakitva.");
                    }
                    break;
                case "3":
                    System.out.println("===== Mentesek exportalasa =====");
                    System.out.println("1. Egy mentes exportalasa");
                    System.out.println("2. Az osszes mentes exportalasa");
                    System.out.print("Opcio: ");
                    String opci = sc.nextLine().trim();

                    if(opci.equals("1")) {
                        GameSL.listSavedGames();
                        System.out.print("Add meg az exportalando mentes ID-jat: ");
                        int id = Integer.parseInt(sc.nextLine());

                        System.out.print("Fajlnev (pl. game.xml): ");
                        String file = sc.nextLine().trim();

                        GameSL.exportOneToXml(id, file);
                    } else if(opci.equals("2")) {
                        System.out.print("Fajlnev (pl. all_games.xml): ");
                        String file = sc.nextLine().trim();

                        GameSL.exportAllToXml(file);
                    } else {
                        System.out.println("Nincs ilyen opcio!");
                    }
                    break;
                case "4":
                    System.out.println("===== Mentesek importalasa =====");
                    System.out.print("Add meg az XML fajl nevet: ");
                    String file = sc.nextLine().trim();

                    GameSL.importFromXml(file);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Nincs ilyen opcio!");
            }
        }
    }
}
