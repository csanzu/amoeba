package org.example.amoeba.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatsSL {


//STATISZTIKÁK MENTÉSE
    public static void ment(String playerName, int width, int height, String winner, int steps) {
        String sql = "INSERT INTO stats(player_name, board_width, board_height, winner, steps) VALUES(?,?,?,?,?)";
        try (Connection conn = DBConnectInit.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, playerName);
            ps.setInt(2, width);
            ps.setInt(3, height);
            ps.setString(4, winner);
            ps.setInt(5, steps);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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

            while (rs.next()) {
                eredmeny.add(new String[] {
                        rs.getInt("board_width") + "x" + rs.getInt("board_height"),
                        String.valueOf(rs.getInt("total_games")),
                        String.valueOf(rs.getInt("ember_wins")),
                        String.valueOf(rs.getInt("gep_wins"))
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eredmeny;
    }
//TÁBLA MÉRET ALAPÚ STATISZTIKA VÉGE


//RANGLISTA WIN ALAPJÁN
    public static List<String[]> ranglista() {
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

            int helyezes = 1;
            while (rs.next()) {
                lista.add(new String[] {
                        String.valueOf(helyezes++),
                        rs.getString("player_name"),
                        String.valueOf(rs.getInt("wins"))
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
//RANGLISTA WIN ALAPJÁN VÉGE


//STATISZTIKA DB TÖRLÉSE
    public static void torolMindent() {
        String sql = "DELETE FROM stats";

        try (Connection conn = DBConnectInit.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//STATISZTIKA DB TÖRLÉSE VÉGE


//ADOTT JÁTÉKOS STATISZTIKÁJÁNAK TÖRLÉSE
    public static void torolJatekos(String playerName) {
        String sql = "DELETE FROM stats WHERE player_name = ?";

        try (Connection conn = DBConnectInit.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, playerName);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//ADOTT JÁTÉKOS STATISZTIKÁJÁNAK TÖRLÉSE

}
