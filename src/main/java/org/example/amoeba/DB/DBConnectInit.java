package org.example.amoeba.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnectInit {

    private static final String DB_URL = "jdbc:sqlite:amoeba.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void init() {
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
                    current_player TEXT NOT NULL,
                    moves TEXT NOT NULL
                )
            """);

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

            System.out.println("DB Init:saved_games tabla kesz");
            System.out.println("DB Init:stats tabla kesz");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("DB Init:Hiba");
        }
    }

}
