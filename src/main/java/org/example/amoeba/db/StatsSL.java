package org.example.amoeba.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatsSL {
    private static final Logger log = LoggerFactory.getLogger(StatsSL.class);

//STATISZTIKÁK MENTÉSE
public static void ment(String playerName, int width, int height, String winner, int steps) {
    log.debug("Statisztika mentése: player={}, winner={}, steps={}, board={}x{}",
            playerName, winner, steps, width, height);

    String sql = "INSERT INTO stats(player_name, board_width, board_height, winner, steps) VALUES(?,?,?,?,?)";
    try (Connection conn = DBConnectInit.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, playerName);
        ps.setInt(2, width);
        ps.setInt(3, height);
        ps.setString(4, winner);
        ps.setInt(5, steps);
        ps.executeUpdate();

        log.info("Statisztika sikeresen mentve: player={}, winner={}", playerName, winner);

    } catch (SQLException e) {
        log.error("Hiba statisztika mentése közben: player=" + playerName + ", winner=" + winner, e);
    } catch (Exception e) {
        log.error("Ismeretlen hiba statisztika mentése közben", e);
    }
}
//STATISZTIKÁK MENTÉSE VÉGE

//LEGTÖBBET NYERT
/*    public static List<String[]> legtobbetNyert() {
        List<String[]> eredmeny = new ArrayList<>();

        String sql = """
        SELECT player_name, COUNT(*) AS wins
        FROM stats
        WHERE winner = 'EMBER'
        GROUP BY player_name
        ORDER BY wins DESC
        LIMIT 10
    """;

        try (Connection conn = DBConnectInit.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                eredmeny.add(new String[]{
                        rs.getString("player_name"),
                        String.valueOf(rs.getInt("wins"))
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eredmeny;
    }*/
//LEGTÖBBET NYERT VÉGE


//LEGKEVESEBB LÉPÉSBŐL
/*    public static List<String[]> legkevesebbLepes(int width, int height) {
        List<String[]> eredmeny = new ArrayList<>();

        String sql = """
        SELECT player_name, MIN(steps) AS min_steps
        FROM stats
        WHERE winner = 'EMBER'
          AND board_width = ?
          AND board_height = ?
        GROUP BY player_name
        ORDER BY min_steps ASC
        LIMIT 10
    """;

        try (Connection conn = DBConnectInit.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, width);
            ps.setInt(2, height);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                eredmeny.add(new String[]{
                        rs.getString("player_name"),
                        String.valueOf(rs.getInt("min_steps"))
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eredmeny;
    }*/
//LEGKEVESEBB LÉPÉSBŐL VÉGE


//TÁBLA MÉRET ALAPÚ STATISZTIKA
public static List<String[]> tablaMeretStatisztika() {
    log.debug("Táblaméret alapú statisztika lekérdezése kezdődik");

    List<String[]> eredmeny = new ArrayList<>();

    String sql = """
        SELECT
            board_width,
            board_height,
            COUNT(*) AS total_games,
            SUM(CASE WHEN winner != 'Gép' THEN 1 ELSE 0 END) AS ember_wins,
            SUM(CASE WHEN winner = 'Gép' THEN 1 ELSE 0 END) AS gep_wins
        FROM stats
        GROUP BY board_width, board_height
        ORDER BY total_games DESC
    """;

    try (Connection conn = DBConnectInit.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        log.trace("SQL lekérdezés fut: {}", sql);

        while (rs.next()) {
            eredmeny.add(new String[] {
                    rs.getInt("board_width") + "x" + rs.getInt("board_height"),
                    String.valueOf(rs.getInt("total_games")),
                    String.valueOf(rs.getInt("ember_wins")),
                    String.valueOf(rs.getInt("gep_wins"))
            });
        }

        log.info("Táblaméret statisztika lekérdezés sikeres, sorok száma: {}", eredmeny.size());

    } catch (SQLException e) {
        log.error("Hiba a táblaméret statisztika lekérdezése közben", e);
    }

    return eredmeny;
}
//TÁBLA MÉRET ALAPÚ STATISZTIKA VÉGE


//RANGLISTA WIN ALAPJÁN
public static List<String[]> ranglista() {
    log.debug("Ranglista lekérdezés kezdődik");

    List<String[]> lista = new ArrayList<>();

    String sql = """
        SELECT player_name, COUNT(*) AS wins
        FROM stats
        WHERE winner = player_name
        GROUP BY player_name
        ORDER BY wins DESC
        LIMIT 10
    """;

    try (Connection conn = DBConnectInit.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        log.trace("SQL lekérdezés fut: {}", sql);

        int helyezes = 1;
        while (rs.next()) {
            lista.add(new String[] {
                    String.valueOf(helyezes++),
                    rs.getString("player_name"),
                    String.valueOf(rs.getInt("wins"))
            });
        }

        log.info("Ranglista lekérdezés sikeres, sorok száma: {}", lista.size());

    } catch (SQLException e) {
        log.error("Hiba a ranglista lekérdezése közben", e);
    }

    return lista;
}
//RANGLISTA WIN ALAPJÁN VÉGE


//STATISZTIKA DB TÖRLÉSE
public static void torolMindent() {
    log.debug("Statisztika tábla törlésének indítása");

    String sql = "DELETE FROM stats";

    try (Connection conn = DBConnectInit.getConnection();
         Statement st = conn.createStatement()) {

        int sorok = st.executeUpdate(sql);
        log.info("Statisztika tábla törölve, érintett sorok száma: {}", sorok);

    } catch (SQLException e) {
        log.error("Hiba a statisztika tábla törlése közben", e);
    }
}
//STATISZTIKA DB TÖRLÉSE VÉGE


//ADOTT JÁTÉKOS STATISZTIKÁJÁNAK TÖRLÉSE
public static void torolJatekos(String playerName) {
    log.debug("Statisztika törlése kezdődik a játékos számára: {}", playerName);

    String sql = "DELETE FROM stats WHERE player_name = ?";

    try (Connection conn = DBConnectInit.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, playerName);
        int sorok = ps.executeUpdate();

        if (sorok > 0) {
            log.info("Statisztikák törölve játékos: {}, érintett sorok száma: {}", playerName, sorok);
        } else {
            log.warn("Nincs statisztika törlendő játékos: {}", playerName);
        }

    } catch (SQLException e) {
        log.error("Hiba történt a statisztikák törlése közben játékos: {}", playerName, e);
    }
}
//ADOTT JÁTÉKOS STATISZTIKÁJÁNAK TÖRLÉSE

}
