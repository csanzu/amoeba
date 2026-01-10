package org.example.amoeba.meccs;

import java.util.ArrayList;

import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;
import org.example.amoeba.vos.TablaMeret;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckWinTest {

    private Tabla createTabla(int width, int height) {
        return new Tabla(new TablaMeret(width, height));
    }

    @Test
    void testNincsGyoztesUresTablan() {
        Tabla tabla = createTabla(5, 5);
        CheckWin checkWin = new CheckWin(tabla, 5);

        assertNull(checkWin.ellenoriz(),
                "Üres táblán nem lehet győztes");
    }

    @Test
    void testVizszintesGyozelem() {
        Tabla tabla = createTabla(5, 5);

        for (int col = 1; col <= 5; col++) {
            tabla.lerak(new Pozicio(3, col), JatekosJel.X);
        }

        CheckWin checkWin = new CheckWin(tabla, 5);

        assertEquals(JatekosJel.X, checkWin.ellenoriz(),
                "Vízszintes győzelem nem lett felismerve");
    }

    @Test
    void testFuggolegesGyozelem() {
        Tabla tabla = createTabla(5, 5);

        for (int row = 1; row <= 5; row++) {
            tabla.lerak(new Pozicio(row, 2), JatekosJel.O);
        }

        CheckWin checkWin = new CheckWin(tabla, 5);

        assertEquals(JatekosJel.O, checkWin.ellenoriz(),
                "Függőleges győzelem nem lett felismerve");
    }

    @Test
    void testAtlosBalFelulrolJobbAlulra() {
        Tabla tabla = createTabla(5, 5);

        for (int i = 1; i <= 5; i++) {
            tabla.lerak(new Pozicio(i, i), JatekosJel.X);
        }

        CheckWin checkWin = new CheckWin(tabla, 5);

        assertEquals(JatekosJel.X, checkWin.ellenoriz(),
                "Átlós (\\) győzelem nem lett felismerve");
    }

    @Test
    void testAtlosJobbFelulrolBalAlulra() {
        Tabla tabla = createTabla(5, 5);

        int col = 5;
        for (int row = 1; row <= 5; row++) {
            tabla.lerak(new Pozicio(row, col--), JatekosJel.O);
        }

        CheckWin checkWin = new CheckWin(tabla, 5);

        assertEquals(JatekosJel.O, checkWin.ellenoriz(),
                "Átlós (/) győzelem nem lett felismerve");
    }

    @Test
    void testDontetlen() {
        Tabla tabla = createTabla(4, 4);

        for (int row = 1; row <= 4; row++) {
            for (int col = 1; col <= 4; col++) {
                tabla.lerak(
                        new Pozicio(row, col),
                        (row + col) % 2 == 0 ? JatekosJel.X : JatekosJel.O
                );
            }
        }

        CheckWin checkWin = new CheckWin(tabla, 4);

        assertTrue(checkWin.dontetlen(),
                "A döntetlen állapot nem lett felismerve");
    }
}
