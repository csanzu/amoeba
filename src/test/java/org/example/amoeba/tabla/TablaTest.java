package org.example.amoeba.tabla;

import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;
import org.example.amoeba.vos.TablaMeret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TablaTest {

    private Tabla tabla;

    @BeforeEach
    void setUp() {
        TablaMeret meret = new TablaMeret(5, 5);
        tabla = new Tabla(meret);
    }

    @Test
    void testGetMeret() {
        assertEquals(5, tabla.getMeret().getSzelesseg());
        assertEquals(5, tabla.getMeret().getMagassag());
    }

    @Test
    void testIsBelul() {
        assertTrue(tabla.isBelul(new Pozicio(1, 1)));
        assertTrue(tabla.isBelul(new Pozicio(5, 5)));
        assertFalse(tabla.isBelul(new Pozicio(0, 1)));
        assertFalse(tabla.isBelul(new Pozicio(1, 0)));
        assertFalse(tabla.isBelul(new Pozicio(6, 1)));
        assertFalse(tabla.isBelul(new Pozicio(1, 6)));
    }

    @Test
    void testIsUres() {
        Pozicio p = new Pozicio(2, 2);
        assertTrue(tabla.isUres(p));
        tabla.lerak(p, JatekosJel.X);
        assertFalse(tabla.isUres(p));
    }

    @Test
    void testGetSzabadPoziciok() {
        tabla.lerak(new Pozicio(1, 1), JatekosJel.X);
        tabla.lerak(new Pozicio(2, 2), JatekosJel.O);
        List<Pozicio> szabad = tabla.getSzabadPoziciok();
        assertEquals(23, szabad.size());
        assertFalse(szabad.contains(new Pozicio(1, 1)));
        assertFalse(szabad.contains(new Pozicio(2, 2)));
    }

    @Test
    void testLerakValidAndInvalid() {
        Pozicio valid = new Pozicio(3, 3);
        tabla.lerak(valid, JatekosJel.X);
        assertEquals(JatekosJel.X, tabla.getPoz(valid));

        // Pozíció táblán kívül
        assertThrows(IllegalArgumentException.class, () -> tabla.lerak(new Pozicio(0, 0), JatekosJel.O));

        // Már foglalt hely
        assertThrows(IllegalArgumentException.class, () -> tabla.lerak(valid, JatekosJel.O));
    }

    @Test
    void testVanSzomszedosJel() {
        // Semmi nincs körülötte
        assertFalse(tabla.vanSzomszedosJel(new Pozicio(3, 3), JatekosJel.X));

        // Lerakunk egy szomszédos jelet
        tabla.lerak(new Pozicio(2, 2), JatekosJel.O);
        assertTrue(tabla.vanSzomszedosJel(new Pozicio(3, 3), JatekosJel.X));

        // Sarok pozíció
        assertTrue(tabla.vanSzomszedosJel(new Pozicio(1, 1), JatekosJel.X));
    }

    @Test
    void testGetOsszesLepes() {
        Pozicio p1 = new Pozicio(1, 1);
        Pozicio p2 = new Pozicio(2, 2);
        tabla.lerak(p1, JatekosJel.X);
        tabla.lerak(p2, JatekosJel.O);

        Map<Pozicio, JatekosJel> lepesek = tabla.getOsszesLepes();
        assertEquals(2, lepesek.size());
        assertEquals(JatekosJel.X, lepesek.get(p1));
        assertEquals(JatekosJel.O, lepesek.get(p2));
    }

    @Test
    void testEdgeCasesAllCorners() {
        // Sarokba lerakás és szomszédok
        tabla.lerak(new Pozicio(1, 1), JatekosJel.X);
        tabla.lerak(new Pozicio(1, 5), JatekosJel.O);
        tabla.lerak(new Pozicio(5, 1), JatekosJel.X);
        tabla.lerak(new Pozicio(5, 5), JatekosJel.O);

        // Szomszédos ellenőrzés minden sarokhoz
        assertTrue(tabla.vanSzomszedosJel(new Pozicio(2, 2), JatekosJel.X));
        assertTrue(tabla.vanSzomszedosJel(new Pozicio(2, 4), JatekosJel.O));
        assertTrue(tabla.vanSzomszedosJel(new Pozicio(4, 2), JatekosJel.X));
        assertTrue(tabla.vanSzomszedosJel(new Pozicio(4, 4), JatekosJel.O));
    }

    @Test
    void testFullBoardSzabadPoziciok() {
        // Tábla teljes feltöltése
        for (int s = 1; s <= tabla.getMeret().getMagassag(); s++) {
            for (int o = 1; o <= tabla.getMeret().getSzelesseg(); o++) {
                tabla.lerak(new Pozicio(s, o), JatekosJel.X);
            }
        }
        List<Pozicio> szabad = tabla.getSzabadPoziciok();
        assertTrue(szabad.isEmpty());
    }
}
