package org.example.amoeba.tabella;

import org.example.amoeba.db.StatsSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatsTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setUpStreams() {
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testTabellaMenuRanglista() {
        // input: 1 (Ranglista)
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // mock StatsSL.ranglista()
        try (MockedStatic<StatsSL> mocked = mockStatic(StatsSL.class)) {
            mocked.when(StatsSL::ranglista).thenReturn(List.of(
                    new String[]{"1", "Alice", "3"},
                    new String[]{"2", "Bob", "1"}
            ));

            Stats.tabellaMenu();

            String output = outContent.toString();
            assertTrue(output.contains("Ranglista") || output.contains("Alice") || output.contains("Bob"));
        }
    }

    @Test
    void testTabellaMenuTorlesVissza() {
        // 3 -> StatsTorlesMenu.megjelenit(), 4 -> vissza
        String input = "4\n"; // vissza
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // egyszerűen ne dobjon hibát
        Stats.tabellaMenu();
        String output = outContent.toString();
        assertTrue(output.contains("STATISZTIKAK"));
    }

    @Test
    void testStatsTorlesMenuMegjelenitTorlMind() {
        String input = "1\nigen\n3\n"; // 1: törlés + igen, 3: vissza
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        try (MockedStatic<StatsSL> mocked = mockStatic(StatsSL.class)) {
            StatsTorlesMenu.megjelenit();

            mocked.verify(StatsSL::torolMindent, times(1));
            String output = outContent.toString();
            assertTrue(output.contains("Minden stat törölve"));
        }
    }

    @Test
    void testTabellaKiir() {
        String[] fejlec = {"Hely", "Játékos"};
        List<String[]> adatok = List.of(
                new String[]{"1", "Alice"},
                new String[]{"2", "Bob"}
        );

        Tabella.kiir(adatok, fejlec);
        String output = outContent.toString();

        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("Bob"));
        assertTrue(output.contains("Hely"));
    }
}
