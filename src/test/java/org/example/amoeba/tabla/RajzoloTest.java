package org.example.amoeba.tabla;

import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;
import org.example.amoeba.vos.TablaMeret;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class RajzoloTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testRajzoloPrintsEmptyBoard() {
        Tabla tabla = new Tabla(new TablaMeret(4, 4));
        Rajzolo rajzolo = new Rajzolo();

        rajzolo.rajzolo(tabla);

        String output = outContent.toString();

        assertTrue(output.contains("Aktualis allas"));
        assertTrue(output.contains("A B C D"));
        assertTrue(output.contains("  1| . . . ."));
        assertTrue(output.contains("  2| . . . ."));
        assertTrue(output.contains("  3| . . . ."));
        assertTrue(output.contains("  4| . . . ."));
    }

    @Test
    void testRajzoloPrintsXAndO() {
        Tabla tabla = new Tabla(new TablaMeret(4, 4));
        tabla.lerak(new Pozicio(1, 1), JatekosJel.X);
        tabla.lerak(new Pozicio(2, 2), JatekosJel.O);

        Rajzolo rajzolo = new Rajzolo();
        rajzolo.rajzolo(tabla);

        String output = outContent.toString();

        assertTrue(output.contains("  1| X . ."));
        assertTrue(output.contains("  2| . O ."));
    }
}
