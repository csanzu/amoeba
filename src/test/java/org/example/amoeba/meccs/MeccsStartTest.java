package org.example.amoeba.meccs;

import org.example.amoeba.db.GameSL;
import org.example.amoeba.jatekos.Jatekos;
import org.example.amoeba.jatekos.Ember;
import org.example.amoeba.jatekos.Gep;
import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;
import org.example.amoeba.vos.TablaMeret;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeccsStartTest {

    private Meccs meccs;
    private GameSL mockGameSL;
    private Jatekos mockEmber;
    private Jatekos mockGep;

    @BeforeEach
    void setUp() {
        mockGameSL = mock(GameSL.class);

        // Mockolt játékosok
        mockEmber = mock(Jatekos.class);
        mockGep = mock(Jatekos.class);

        // Jelzések
        when(mockEmber.getJel()).thenReturn(JatekosJel.X);
        when(mockEmber.getNev()).thenReturn("TesztJatekos");
        when(mockGep.getJel()).thenReturn(JatekosJel.O);
        when(mockGep.getNev()).thenReturn("Gép");

        // Tábla és Meccs inicializálása
        TablaMeret meret = new TablaMeret(5,5);
        Tabla tabla = new Tabla(meret);

        meccs = new Meccs(mockGameSL);
        meccs.setMeret(meret);
        meccs.setTabla(tabla);
        meccs.setEmber(mockEmber);
        meccs.setGep(mockGep);
        meccs.setAktualis(mockEmber);
    }

    @Test
    void testStartGameWithMockedMoves_XWinsHorizontally() {
        // X (ember) lépései vízszintesen nyeréshez
        Pozicio[] emberMoves = {
                new Pozicio(1,1),
                new Pozicio(1,2),
                new Pozicio(1,3),
                new Pozicio(1,4),
                new Pozicio(1,5)
        };

        // O (gep) lépései, nem zavarják a győzelmet
        Pozicio[] gepMoves = {
                new Pozicio(2,1),
                new Pozicio(2,2),
                new Pozicio(2,3),
                new Pozicio(2,4)
        };

        // Mock lépések
        when(mockEmber.lep(any(Tabla.class)))
                .thenReturn(emberMoves[0], emberMoves[1], emberMoves[2], emberMoves[3], emberMoves[4]);

        when(mockGep.lep(any(Tabla.class)))
                .thenReturn(gepMoves[0], gepMoves[1], gepMoves[2], gepMoves[3]);

        // Futtassuk a játékot
        meccs.start();

        // Ellenőrizzük, hogy a tábla tartalmazza a lépéseket
        Tabla tabla = meccs.getTabla();
        for (Pozicio p : emberMoves) {
            assertEquals(JatekosJel.X, tabla.getPoz(p));
        }
        for (Pozicio p : gepMoves) {
            assertEquals(JatekosJel.O, tabla.getPoz(p));
        }
    }

    @Test
    void testStartGameDraw() {
        // Mindkét játékos lépései döntetlenhez
        Pozicio[] moves = {
                new Pozicio(1,1), new Pozicio(1,2), new Pozicio(1,3),
                new Pozicio(1,4), new Pozicio(1,5), new Pozicio(2,1),
                new Pozicio(2,2), new Pozicio(2,3), new Pozicio(2,4),
                new Pozicio(2,5), new Pozicio(3,1), new Pozicio(3,2),
                new Pozicio(3,3), new Pozicio(3,4), new Pozicio(3,5),
                new Pozicio(4,1), new Pozicio(4,2), new Pozicio(4,3),
                new Pozicio(4,4), new Pozicio(4,5), new Pozicio(5,1),
                new Pozicio(5,2), new Pozicio(5,3), new Pozicio(5,4),
                new Pozicio(5,5)
        };

        // Alternáló lépések ember/ gép
        when(mockEmber.lep(any(Tabla.class)))
                .thenReturn(moves[1], moves[2], moves[4], moves[6], moves[8], moves[10], moves[12], moves[14], moves[16], moves[18], moves[19], moves[22], moves[24]);
        when(mockGep.lep(any(Tabla.class)))
                .thenReturn(moves[0], moves[3], moves[5], moves[7], moves[9], moves[11], moves[13], moves[15], moves[17], moves[20], moves[21], moves[23]);

        // Futtassuk a játékot
        meccs.start();

        // Tábla ellenőrzése
        Tabla tabla = meccs.getTabla();
        for (Pozicio p : moves) {
            assertNotNull(tabla.getPoz(p));
        }
    }
    @Test
    void testSaveTriggeredWhenPlayerReturnsNull() {
        when(mockEmber.lep(any(Tabla.class))).thenReturn(null);

        meccs.start();

        verify(mockGameSL).save(any(JatekAllapot.class));
    }

}
