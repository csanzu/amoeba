package org.example.amoeba.db;

import java.util.List;
import java.util.ArrayList;

import org.example.amoeba.tabella.Stats;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatsSLTest {
    private StatsSL statsSL = new StatsSL();

    @BeforeEach
    void initDb() {
        DBConnectInit.setDbUrl("jdbc:sqlite:target/test.db"); // fájl alapú teszt DB
        DBConnectInit.init();  // táblák létrehozása
        statsSL.torolMindent();
    }

    @Test
    void testMentCreatesStatRecord() {

        StatsSL.ment(
                "TesztJatekos",
                10,
                10,
                "TesztJatekos",
                15
        );

        List<String[]> ranglista = StatsSL.ranglista();

        assertEquals(1, ranglista.size());
        assertEquals("TesztJatekos", ranglista.get(0)[1]);
        assertEquals("1", ranglista.get(0)[2]); // 1 win
    }

    @Test
    void testTablaMeretStatisztika() {

        StatsSL.ment("A", 5, 5, "A", 10);
        StatsSL.ment("B", 5, 5, "Gép", 12);
        StatsSL.ment("A", 6, 6, "A", 14);

        List<String[]> stat = StatsSL.tablaMeretStatisztika();

        assertEquals(2, stat.size());

        String[] first = stat.get(0);
        assertEquals("5x5", first[0]);
        assertEquals("2", first[1]); // total games
    }

    @Test
    void testRanglista() {

        StatsSL.ment("Anna", 5, 5, "Anna", 10);
        StatsSL.ment("Anna", 5, 5, "Anna", 12);
        StatsSL.ment("Bela", 5, 5, "Bela", 11);

        List<String[]> lista = StatsSL.ranglista();

        assertEquals(2, lista.size());
        assertEquals("Anna", lista.get(0)[1]);
        assertEquals("2", lista.get(0)[2]);
    }

    @Test
    void testTorolJatekos() {

        StatsSL.ment("Torolando", 5, 5, "Torolando", 10);
        StatsSL.ment("Marad", 5, 5, "Marad", 10);

        StatsSL.torolJatekos("Torolando");

        List<String[]> lista = StatsSL.ranglista();

        assertEquals(1, lista.size());
        assertEquals("Marad", lista.get(0)[1]);
    }

    @Test
    void testTorolMindent() {

        StatsSL.ment("A", 5, 5, "A", 10);
        StatsSL.ment("B", 5, 5, "B", 10);

        StatsSL.torolMindent();

        assertTrue(StatsSL.ranglista().isEmpty());
        assertTrue(StatsSL.tablaMeretStatisztika().isEmpty());
    }
}





