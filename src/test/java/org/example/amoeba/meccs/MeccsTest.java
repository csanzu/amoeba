package org.example.amoeba.meccs;

import org.example.amoeba.db.GameSL;
import org.example.amoeba.jatekos.Ember;
import org.example.amoeba.jatekos.Gep;
import org.example.amoeba.jatekos.Jatekos;
import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;
import org.example.amoeba.vos.TablaMeret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeccsTest {

    private Meccs meccs;
    private GameSL mockGameSL;

    @BeforeEach
    void setUp() throws Exception {
        // Mock a GameSL-t, hogy a teszt ne írjon DB-be
        mockGameSL = mock(GameSL.class);

        // Meccs példány létrehozása
        meccs = new Meccs(mockGameSL);

        TablaMeret meret = new TablaMeret(5, 5);
        Tabla tabla = new Tabla(meret);
        Jatekos ember = new Ember("TesztJatekos");
        Jatekos gep = new Gep();

        meccs.setMeret(meret);
        meccs.setTabla(tabla);
        meccs.setEmber(ember);
        meccs.setGep(gep);
        meccs.setAktualis(ember);

        // kezdő X-et a középre
        Pozicio kozep = new Pozicio(3, 3);
        tabla.lerak(kozep, JatekosJel.X);
    }

    @Test
    void testEmberGepInitialization() {
        assertNotNull(meccs.getEmber());
        assertNotNull(meccs.getGep());

        assertEquals(JatekosJel.X, meccs.getEmber().getJel());
        assertEquals(JatekosJel.O, meccs.getGep().getJel());
    }

    @Test
    void testTablaInitialization() {
        Tabla tabla = meccs.getTabla();
        assertNotNull(tabla);

        Pozicio kozep = new Pozicio(3, 3);
        assertEquals(JatekosJel.X, tabla.getPoz(kozep));
    }

    @Test
    void testExportAllapot() {
        JatekAllapot allapot = meccs.exportAllapot();

        assertNotNull(allapot);
        assertEquals(meccs.getEmber().getNev(), allapot.getEmberNev());
        assertEquals(meccs.getAktualis().getJel(), allapot.getAktualisJel());
        assertFalse(allapot.getLepesek().isEmpty());
    }

    @Test
    void testSaveCalledOnExport() {
        JatekAllapot allapot = meccs.exportAllapot();

        // Hívjuk a mock GameSL save-t
        meccs.getGameSL().save(allapot);

        // Ellenőrizzük, hogy meghívódott
        verify(mockGameSL, times(1)).save(allapot);
    }

    @Test
    void testCheckWinDetection() {
        // Tegyünk vízszintes győzelmet X-el
        Tabla tabla = meccs.getTabla();
        tabla.lerak(new Pozicio(3, 2), JatekosJel.X);
        tabla.lerak(new Pozicio(3, 4), JatekosJel.X);
        tabla.lerak(new Pozicio(3, 1), JatekosJel.X);
        tabla.lerak(new Pozicio(3, 5), JatekosJel.X);

        CheckWin check = new CheckWin(tabla, 5);
        assertEquals(JatekosJel.X, check.ellenoriz());
    }

    @Test
    void testDontetlenDetection() {
        TablaMeret meret = new TablaMeret(4,4);
        Tabla tabla = new Tabla(meret);

        // Alternáló jelek
        JatekosJel[][] jelek = {
                {JatekosJel.X, JatekosJel.O, JatekosJel.X, JatekosJel.O},
                {JatekosJel.O, JatekosJel.X, JatekosJel.O, JatekosJel.X},
                {JatekosJel.O, JatekosJel.X, JatekosJel.O, JatekosJel.X},
                {JatekosJel.O, JatekosJel.X, JatekosJel.O, JatekosJel.X}
        };

        for (int s = 0; s < 4; s++) {
            for (int o = 0; o < 4; o++) {
                tabla.lerak(new Pozicio(s+1, o+1), jelek[s][o]);
            }
        }

        CheckWin check = new CheckWin(tabla, 4);
        assertTrue(check.dontetlen());
        assertNull(check.ellenoriz());
    }

    @Test
    void testAktualisSwitching() {
        Jatekos elso = meccs.getAktualis();
        meccs.setAktualis(meccs.getGep());
        assertNotEquals(elso, meccs.getAktualis());
    }

    @Test
    void testLerakAndInvalidPosition() {
        Tabla tabla = meccs.getTabla();

        // Érvénytelen pozíció (táblán kívül)
        Pozicio out = new Pozicio(10,10);
        assertThrows(IllegalArgumentException.class, () -> tabla.lerak(out, JatekosJel.X));

        // Érvényes pozíció, üres
        Pozicio valid = new Pozicio(1,1);
        tabla.lerak(valid, JatekosJel.O);
        assertEquals(JatekosJel.O, tabla.getPoz(valid));
    }

    @Test
    void testSetAndGetTabla() {
        Tabla ujTabla = new Tabla(new TablaMeret(6, 6));

        meccs.setTabla(ujTabla);

        assertSame(ujTabla, meccs.getTabla());
    }

    @Test
    void testSetAndGetEmber() {
        Jatekos ujEmber = new Ember("ÚjEmber");

        meccs.setEmber(ujEmber);

        assertSame(ujEmber, meccs.getEmber());
    }

    @Test
    void testSetAndGetGep() {
        Jatekos ujGep = new Gep();

        meccs.setGep(ujGep);

        assertSame(ujGep, meccs.getGep());
    }

    @Test
    void testSetAndGetAktualis() {
        Jatekos aktualis = meccs.getGep();

        meccs.setAktualis(aktualis);

        assertSame(aktualis, meccs.getAktualis());
    }

    @Test
    void testSetAndGetMeret() {
        TablaMeret ujMeret = new TablaMeret(7, 8);

        meccs.setMeret(ujMeret);

        assertSame(ujMeret, meccs.getMeret());
    }

    @Test
    void testGetGameSL() {
        assertSame(mockGameSL, meccs.getGameSL());
    }

    @Test
    void testExportAllapotFully() {
        // GIVEN
        TablaMeret meret = new TablaMeret(5, 5);
        Tabla tabla = new Tabla(meret);

        tabla.lerak(new Pozicio(2, 2), JatekosJel.X);
        tabla.lerak(new Pozicio(3, 3), JatekosJel.O);

        Jatekos ember = new Ember("TesztJatekos");
        Jatekos gep = new Gep();

        meccs.setMeret(meret);
        meccs.setTabla(tabla);
        meccs.setEmber(ember);
        meccs.setGep(gep);
        meccs.setAktualis(gep);

        // WHEN
        JatekAllapot allapot = meccs.exportAllapot();

        // THEN
        assertNotNull(allapot);
        assertEquals(meret, allapot.getMeret());
        assertEquals("TesztJatekos", allapot.getEmberNev());
        assertEquals(JatekosJel.O, allapot.getAktualisJel());

        assertEquals(2, allapot.getLepesek().size());
        assertEquals(JatekosJel.X, allapot.getLepesek().get(new Pozicio(2, 2)));
        assertEquals(JatekosJel.O, allapot.getLepesek().get(new Pozicio(3, 3)));
    }

    @Test
    void testNevBeker_UresMajdHelyesNev() throws Exception {
        String input = "\nTesztJatekos\n";
        Scanner scanner = new Scanner(input);

        meccs.setScanner(scanner);

        Method m = Meccs.class.getDeclaredMethod("nevBeker");
        m.setAccessible(true);

        String nev = (String) m.invoke(meccs);

        assertEquals("TesztJatekos", nev);
    }

    @Test
    void testBekerMeret_InvalidThenValid() throws Exception {
        String input = "abc\n2\n30\n10\n";
        Scanner scanner = new Scanner(input);

        meccs.setScanner(scanner);

        Method m = Meccs.class.getDeclaredMethod("bekerMeret", String.class);
        m.setAccessible(true);

        int result = (int) m.invoke(meccs, "Szélesség: ");

        assertEquals(10, result);
    }

    @Test
    void testUjJatek() throws Exception {
        // GIVEN
        String input = String.join("\n",
                "TesztJatekos", // nev
                "10",           // szelesseg
                "10"            // magassag
        );

        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Meccs meccs = new Meccs();

        // private metódus meghívása reflectionnel
        Method ujJatek = Meccs.class.getDeclaredMethod("ujJatek");
        ujJatek.setAccessible(true);

        // WHEN
        ujJatek.invoke(meccs);

        // THEN
        assertNotNull(meccs.getEmber());
        assertEquals("TesztJatekos", meccs.getEmber().getNev());
        assertEquals(JatekosJel.X, meccs.getEmber().getJel());

        assertNotNull(meccs.getGep());
        assertEquals(JatekosJel.O, meccs.getGep().getJel());

        assertNotNull(meccs.getTabla());
        assertEquals(10, meccs.getTabla().getMeret().getSzelesseg());
        assertEquals(10, meccs.getTabla().getMeret().getMagassag());

        // középen X van
        TablaMeret meret = meccs.getMeret();

        int centerRow = (meret.getMagassag() + 1) / 2;
        int centerCol = (meret.getSzelesseg() + 1) / 2;

        Pozicio center = new Pozicio(centerRow, centerCol);

        assertEquals(
                JatekosJel.X,
                meccs.getTabla().getPoz(center),
                "A középső mezőn X-nek kell lennie"
        );

        // aktuális játékos a gép
        assertEquals(meccs.getGep(), meccs.getAktualis());

        System.setIn(originalIn);
    }


}
