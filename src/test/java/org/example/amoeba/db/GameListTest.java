package org.example.amoeba.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GameListTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testListSavedGamesWithData() throws Exception {
        // Mock ResultSet
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(true, true, false); // két sor, majd vége
        when(rs.getInt("id")).thenReturn(1, 2);
        when(rs.getString("player_name")).thenReturn("Alice", "Bob");
        when(rs.getString("saved_at")).thenReturn("2026-01-10", "2026-01-11");

        // Mock Statement
        Statement st = mock(Statement.class);
        when(st.executeQuery(anyString())).thenReturn(rs);

        // Mock Connection
        Connection c = mock(Connection.class);
        when(c.createStatement()).thenReturn(st);

        // Mock static DBConnectInit.getConnection()
        try (MockedStatic<DBConnectInit> dbMock = mockStatic(DBConnectInit.class)) {
            dbMock.when(DBConnectInit::getConnection).thenReturn(c);

            GameSL.listSavedGames();

            String output = outContent.toString();
            assertTrue(output.contains("1 | Alice | 2026-01-10"));
            assertTrue(output.contains("2 | Bob | 2026-01-11"));
        }
    }

    @Test
    void testListSavedGamesEmpty() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.next()).thenReturn(false); // nincs adat

        Statement st = mock(Statement.class);
        when(st.executeQuery(anyString())).thenReturn(rs);

        Connection c = mock(Connection.class);
        when(c.createStatement()).thenReturn(st);

        try (MockedStatic<DBConnectInit> dbMock = mockStatic(DBConnectInit.class)) {
            dbMock.when(DBConnectInit::getConnection).thenReturn(c);

            GameSL.listSavedGames();

            String output = outContent.toString();
            assertTrue(output.contains("ID | Játékos | Dátum")); // fejléc
            // Bár a log a warn, mi a konzolon nem látjuk, de a metódus logolja
        }
    }
}
