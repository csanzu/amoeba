package org.example.amoeba.db;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import java.util.ArrayList;

import org.example.amoeba.meccs.JatekAllapot;
import org.example.amoeba.vos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.jupiter.api.Assertions.*;

class GameSLTest {

    private GameSL gameSL;

    @BeforeEach
    void setUp() {
        String testDb = "jdbc:sqlite:target/test_import.db";
        DBConnectInit.setDbUrl(testDb);
        DBConnectInit.init();  // táblák létrehozása

        gameSL = new GameSL();
        GameSL.deleteAll();
    }

    @Test
    void testSaveAndLoadGame() {

        TablaMeret meret = new TablaMeret(5, 5);
        Map<Pozicio, JatekosJel> lepesek = Map.of(
                new Pozicio(3, 3), JatekosJel.X,
                new Pozicio(3, 4), JatekosJel.O
        );

        JatekAllapot allapot = new JatekAllapot(
                meret,
                lepesek,
                "TesztJátékos",
                JatekosJel.O
        );

        gameSL.save(allapot);

        var saves = gameSL.listSaves();
        assertEquals(1, saves.size());

        int id = saves.get(0).id();

        JatekAllapot betoltott = gameSL.load(id);

        assertNotNull(betoltott);
        assertEquals("TesztJátékos", betoltott.getEmberNev());
        assertEquals(JatekosJel.O, betoltott.getAktualisJel());
        assertEquals(2, betoltott.getLepesek().size());
    }

    @Test
    void testDeleteAll() {

        TablaMeret meret = new TablaMeret(5, 5);
        JatekAllapot allapot = new JatekAllapot(
                meret,
                Map.of(new Pozicio(1, 1), JatekosJel.X),
                "Player",
                JatekosJel.X
        );

        gameSL.save(allapot);
        assertFalse(gameSL.listSaves().isEmpty());

        GameSL.deleteAll();

        assertTrue(gameSL.listSaves().isEmpty());
    }

    @Test
    void testImportFromXml() throws Exception {
        // Teszt XML létrehozása
        String file = "target/test_import.xml";
        String xmlContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <savedGames>
                <savedGame>
                    <id>1</id>
                    <playerName>ImportPlayer</playerName>
                    <saved_at></saved_at>
                    <boardWidth>5</boardWidth>
                    <boardHeight>5</boardHeight>
                    <winner>X</winner>
                    <current_player>X</current_player>
                    <moves>0</moves>
                </savedGame>
            </savedGames>
            """;

        java.nio.file.Files.writeString(java.nio.file.Paths.get(file), xmlContent);

        // Ürítjük a táblát
        GameSL.deleteAll();
        assertEquals(0, gameSL.listSaves().size());

        // Importálás
        GameSL.importFromXml(file);

        // Ellenőrzés: 1 mentés került be
        var saves = gameSL.listSaves();
        assertEquals(1, saves.size());

        var imported = gameSL.load(saves.get(0).id());
        assertNotNull(imported);
        assertEquals("ImportPlayer", imported.getEmberNev());
        assertEquals(5, imported.getMeret().getSzelesseg());
        assertEquals(5, imported.getMeret().getMagassag());
        assertEquals("X", imported.getAktualisJel().name());
        assertEquals("X", imported.getAktualisJel().name());
        assertEquals(0, imported.getLepesek().size());
    }

    @Test
    void testExportAllToXmlWithDbUrl() throws Exception {

        String dbUrl = "jdbc:sqlite:target/test_export.db";
        String xmlFile = "target/test_export_all.xml";

        // --- DB init ---
        DBConnectInit.setDbUrl(dbUrl);
        DBConnectInit.init();
        GameSL.deleteAll();

        GameSL gameSL = new GameSL();

        // --- mentés beszúrása ---
        TablaMeret meret = new TablaMeret(5, 5);
        Map<Pozicio, JatekosJel> lepesek = Map.of(
                new Pozicio(3, 3), JatekosJel.X,
                new Pozicio(3, 4), JatekosJel.O
        );

        JatekAllapot allapot = new JatekAllapot(
                meret,
                lepesek,
                "TesztJatekos",
                JatekosJel.X
        );

        gameSL.save(allapot);

        assertEquals(1, gameSL.listSaves().size(), "Mentés nem került DB-be");

        // --- XML export ---
        GameSL.exportAllToXml(xmlFile, dbUrl);

        File f = new File(xmlFile);
        assertTrue(f.exists(), "XML fájl nem jött létre");

        // --- XML tartalom ellenőrzés ---
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(f);
        doc.getDocumentElement().normalize();

        NodeList games = doc.getElementsByTagName("savedGame");
        assertEquals(1, games.getLength(), "Nem 1 mentés van az XML-ben");

        Element game = (Element) games.item(0);

        assertEquals("TesztJatekos",
                game.getElementsByTagName("playerName").item(0).getTextContent());

        assertEquals("5",
                game.getElementsByTagName("boardWidth").item(0).getTextContent());

        assertEquals("5",
                game.getElementsByTagName("boardHeight").item(0).getTextContent());

        String winner =
                game.getElementsByTagName("winner").item(0).getTextContent();

        assertTrue(winner == null || winner.isEmpty());

        assertTrue(
                game.getElementsByTagName("moves").item(0).getTextContent().contains("3,3"),
                "Lépések nem kerültek exportálásra"
        );
    }

    @Test
    void testExportOneToXml() throws Exception {
        // GIVEN
        String dbUrl = "jdbc:sqlite:target/test_export_one.db";
        DBConnectInit.setDbUrl(dbUrl);

        try (Connection c = DBConnectInit.getConnection();
             Statement st = c.createStatement()) {

            st.execute("""
            CREATE TABLE IF NOT EXISTS saved_games (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player_name TEXT NOT NULL,
                saved_at TEXT NOT NULL,
                board_width INTEGER NOT NULL,
                board_height INTEGER NOT NULL,
                winner TEXT,
                moves INTEGER NOT NULL
            )
        """);

            st.execute("""
            INSERT INTO saved_games
            (player_name, saved_at, board_width, board_height, winner, moves)
            VALUES ('TesztJatekos', '2024-01-01', 10, 10, 'X', 15)
        """);
        }

        int gameId = 1;
        String xmlFile = "target/export_one_test.xml";

        // WHEN
        GameSL.exportOneToXml(gameId, xmlFile);

        // THEN
        File f = new File(xmlFile);
        assertTrue(f.exists(), "XML fájl nem jött létre");

        Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(f);

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        assertEquals("savedGame", root.getNodeName());

        assertEquals("1",
                root.getElementsByTagName("id").item(0).getTextContent());

        assertEquals("TesztJatekos",
                root.getElementsByTagName("playerName").item(0).getTextContent());

        assertEquals("10",
                root.getElementsByTagName("boardWidth").item(0).getTextContent());

        assertEquals("10",
                root.getElementsByTagName("boardHeight").item(0).getTextContent());

        assertEquals("X",
                root.getElementsByTagName("winner").item(0).getTextContent());

        assertEquals("15",
                root.getElementsByTagName("moves").item(0).getTextContent());

        assertEquals("2024-01-01",
                root.getElementsByTagName("saved_at").item(0).getTextContent());
    }



}


