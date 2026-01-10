package org.example.amoeba.meccs;

import java.util.ArrayList;

import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;
import org.example.amoeba.vos.TablaMeret;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LepesTest {

    private Tabla createTabla(int width, int height) {
        return new Tabla(new TablaMeret(width, height));
    }

    @Test
    void testErvenyesLepesSzomszedosJellel() {
        Tabla tabla = createTabla(5, 5);

        // kezdő lépés
        tabla.lerak(new Pozicio(3, 3), JatekosJel.X);

        // szomszédos lépés
        Lepes lepes = new Lepes(new Pozicio(3, 4), JatekosJel.X);

        assertTrue(lepes.ervenyes(tabla),
                "Szomszédos mezőre tett lépésnek érvényesnek kell lennie");
    }

    @Test
    void testErvenytelenLepesNincsSzomszed() {
        Tabla tabla = createTabla(5, 5);

        Lepes lepes = new Lepes(new Pozicio(1, 1), JatekosJel.X);

        assertFalse(lepes.ervenyes(tabla),
                "Szomszéd nélküli lépés nem lehet érvényes");
    }

    @Test
    void testErvenytelenLepesFoglaltMezo() {
        Tabla tabla = createTabla(5, 5);

        Pozicio poz = new Pozicio(3, 3);
        tabla.lerak(poz, JatekosJel.X);

        Lepes lepes = new Lepes(poz, JatekosJel.O);

        assertFalse(lepes.ervenyes(tabla),
                "Foglalt mezőre tett lépés nem lehet érvényes");
    }

    @Test
    void testErvenytelenLepesTablanKivul() {
        Tabla tabla = createTabla(5, 5);

        Lepes lepes = new Lepes(new Pozicio(0, 3), JatekosJel.X);

        assertFalse(lepes.ervenyes(tabla),
                "Táblán kívüli lépés nem lehet érvényes");
    }

    @Test
    void testErvenyesAtlosSzomszed() {
        Tabla tabla = createTabla(5, 5);

        tabla.lerak(new Pozicio(2, 2), JatekosJel.O);

        Lepes lepes = new Lepes(new Pozicio(3, 3), JatekosJel.O);

        assertTrue(lepes.ervenyes(tabla),
                "Átlós szomszédos lépésnek érvényesnek kell lennie");
    }
}
