package org.example.amoeba.jatekos;

import org.example.amoeba.vos.JatekosJel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmberTest {

    @Test
    void testConstructorSetsNameAndJel() {
        Ember ember = new Ember("TesztElek");

        assertEquals("TesztElek", ember.getNev());
        assertEquals(JatekosJel.X, ember.getJel());
    }

    @Test
    void testGetJelAlwaysX() {
        Ember ember = new Ember("Valaki");

        assertSame(JatekosJel.X, ember.getJel());
    }
}
