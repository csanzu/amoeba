package org.example.amoeba.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectInit {

    private static final Logger log =
            LoggerFactory.getLogger(DBConnectInit.class);

    private static String DB_URL = "jdbc:sqlite:amoeba.db";

    public static Connection getConnection() throws SQLException {
        log.debug("DB kapcsolat nyitása: {}", DB_URL);
        return DriverManager.getConnection(DB_URL);
    }

    //teszt miatt kell
    public static void setDbUrl(String url) {
        DB_URL = url;
    }

    public static void init() {
        log.info("Adatbázis inicializálás indítása");

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            //MENTETT JÁTÉK TÁBLÁJA
            st.execute("""
                CREATE TABLE IF NOT EXISTS saved_games (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    player_name TEXT NOT NULL,
                    saved_at TEXT NOT NULL,
                    board_width INTEGER NOT NULL,
                    board_height INTEGER NOT NULL,
                    winner TEXT,
                    current_player TEXT NOT NULL,
                    moves TEXT NOT NULL
                )
            """);
            log.info("saved_games tábla kész");

            //STATISZTIKA TÁBLÁJA
            st.execute("""
            CREATE TABLE IF NOT EXISTS stats (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    player_name TEXT,
                    board_width INTEGER,
                    board_height INTEGER,
                    winner TEXT,
                    steps INTEGER,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            log.info("stats tábla kész");

            System.out.println("DB Init:saved_games tabla kesz");
            System.out.println("DB Init:stats tabla kesz");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("DB Init:Hiba");
        }
    }

}
