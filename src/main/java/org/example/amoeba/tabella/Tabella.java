package org.example.amoeba.tabella;

import java.util.List;

public class Tabella {

    public static void kiir(List<String[]> adatok, String[] fejlec) {
        for(String f : fejlec) System.out.printf("%-20s", f);
        System.out.println();
        for(int i=0;i<fejlec.length;i++) System.out.print("--------------------");
        System.out.println();

        for(String[] sor : adatok) {
            for(String cella : sor) System.out.printf("%-20s", cella);
            System.out.println();
        }
        System.out.println();
    }
}
