package org.example.amoeba.DB;

import org.example.amoeba.meccs.JatekAllapot;
import org.example.amoeba.vos.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;

public class GameSL {

    private static final String DB_URL = "jdbc:sqlite:amoeba.db";


//JÁTÉK ÁLLAPOT DB-BE
    public void save(JatekAllapot allapot) {

        String sql = """
        INSERT INTO saved_games
        (player_name, saved_at, board_width, board_height, current_player, moves)
        VALUES (?, datetime('now'), ?, ?, ?, ?)
    """;

        try (Connection c = DBConnectInit.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, allapot.getEmberNev());
            ps.setInt(2, allapot.getMeret().getSzelesseg());
            ps.setInt(3, allapot.getMeret().getMagassag());
            ps.setString(4, allapot.getAktualisJel().name());
            ps.setString(5, serializeMoves(allapot.getLepesek()));

            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
//JÁTÉK ÁLLAPOT DB-BE VÉGE


//MENTÉS BETÖLTŐ
    public JatekAllapot load(int id) {

        String sql = "SELECT * FROM saved_games WHERE id = ?";

        try (Connection c = DBConnectInit.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            TablaMeret meret = new TablaMeret(
                    rs.getInt("board_width"),
                    rs.getInt("board_height")
            );

            return new JatekAllapot(
                    meret,
                    deserializeMoves(rs.getString("moves")),
                    rs.getString("player_name"),
                    JatekosJel.valueOf(rs.getString("current_player"))
            );
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
//MENTÉS BETÖLTŐ VÉGE


//JÁTÉK ÁLLAPOT XML-BE
    private String serializeMoves(Map<Pozicio, JatekosJel> lepesek) {
        StringBuilder sb = new StringBuilder();

        for (var e : lepesek.entrySet()) {
            Pozicio p = e.getKey();
            sb.append(p.getSor())
                    .append(",")
                    .append(p.getOszlop())
                    .append(",")
                    .append(e.getValue().name())
                    .append(";");
        }
        return sb.toString();
    }
//JÁTÉK ÁLLAPOT XML-BE VÉGE


//JÁTÉK ÁLLAPOT XML-BŐL
    private Map<Pozicio, JatekosJel> deserializeMoves(String data) {

        Map<Pozicio, JatekosJel> map = new HashMap<>();

        if (data == null || data.isEmpty()) return map;

        for (String part : data.split(";")) {
            String[] p = part.split(",");
            map.put(
                    new Pozicio(
                            Integer.parseInt(p[0]),
                            Integer.parseInt(p[1])
                    ),
                    JatekosJel.valueOf(p[2])
            );
        }
        return map;
    }
//JÁTÉK ÁLLAPOT XML-BŐL VÉGE


//MENTÉS KILISTÁZÓ
    public List<SavedGameInfo> listSaves() {

        List<SavedGameInfo> list = new ArrayList<>();

        String sql = """
        SELECT id, player_name, saved_at
        FROM saved_games
        ORDER BY saved_at DESC
    """;

        try (Connection c = DBConnectInit.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new SavedGameInfo(
                        rs.getInt("id"),
                        rs.getString("player_name"),
                        rs.getString("saved_at")
                ));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
//MENTÉS KILISTÁZÓ VÉGE


//ÖSSZES MENTÉS TÖRLŐ
    public static void deleteAll() {
        String sql = "DELETE FROM saved_games";

        try (Connection c = DBConnectInit.getConnection();
             Statement st = c.createStatement()) {

            st.executeUpdate(sql);
            System.out.println("Minden mentett játék törölve ✔");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//ÖSSZES MENTÉS TÖRLŐ VÉGE


//EXPORT ÖSSZES XML
    public static void exportAllToXml(String fileName) {
        String sql = "SELECT * FROM saved_games";

        try (Connection c = DBConnectInit.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("savedGames");
            doc.appendChild(root);

            while (rs.next()) {
                Element game = doc.createElement("savedGame");
                root.appendChild(game);

                game.appendChild(elem(doc, "id", rs.getString("id")));
                game.appendChild(elem(doc, "playerName", rs.getString("player_name")));
                game.appendChild(elem(doc, "boardWidth", rs.getString("board_width")));
                game.appendChild(elem(doc, "boardHeight", rs.getString("board_height")));
                game.appendChild(elem(doc, "winner", rs.getString("winner")));
                game.appendChild(elem(doc, "steps", rs.getString("steps")));
                game.appendChild(elem(doc, "createdAt", rs.getString("created_at")));
            }

            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(doc), new StreamResult(new File(fileName)));

            System.out.println("Összes mentés exportálva: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//EXPORT ÖSSZES XML VÉGE


//IMPORT XML
    public static void importFromXml(String fileName) {
        try {
            File f = new File(fileName);
            if(!f.exists()) {
                System.out.println("A fájl nem található: " + fileName);
                return;
            }

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(f);
            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("savedGame");
            if(nodes.getLength() == 0) {
                System.out.println("Nincs mentés a fájlban.");
                return;
            }

            try (Connection c = DBConnectInit.getConnection()) {
                String sql = "INSERT INTO saved_games(player_name, board_width, board_height, winner, steps, created_at) " +
                        "VALUES(?,?,?,?,?,?)";
                PreparedStatement ps = c.prepareStatement(sql);

                for(int i=0; i<nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    if(node.getNodeType() != Node.ELEMENT_NODE) continue;
                    Element e = (Element) node;

                    ps.setString(1, getTagValue("playerName", e));
                    ps.setInt(2, Integer.parseInt(getTagValue("boardWidth", e)));
                    ps.setInt(3, Integer.parseInt(getTagValue("boardHeight", e)));
                    ps.setString(4, getTagValue("winner", e));
                    ps.setInt(5, Integer.parseInt(getTagValue("steps", e)));
                    ps.setString(6, getTagValue("createdAt", e));
                    ps.addBatch();
                }

                ps.executeBatch();
                System.out.println("Mentés(ek) importálva: " + nodes.getLength());

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static String getTagValue(String tag, Element e) {
        NodeList nl = e.getElementsByTagName(tag);
        if(nl != null && nl.getLength() > 0 && nl.item(0).getFirstChild() != null) {
            return nl.item(0).getFirstChild().getNodeValue();
        }
        return "";
    }
//IMPORT XML VÉGE


//EXPORT EGYESEVEL XML
    public static void exportOneToXml(int gameId, String fileName) {
        String sql = "SELECT * FROM saved_games WHERE id = ?";

        try (Connection c = DBConnectInit.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, gameId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Nincs ilyen ID-jű mentés!");
                return;
            }

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("savedGame");
            doc.appendChild(root);

            root.appendChild(elem(doc, "id", rs.getString("id")));
            root.appendChild(elem(doc, "playerName", rs.getString("player_name")));
            root.appendChild(elem(doc, "boardWidth", rs.getString("board_width")));
            root.appendChild(elem(doc, "boardHeight", rs.getString("board_height")));
            root.appendChild(elem(doc, "winner", rs.getString("winner")));
            root.appendChild(elem(doc, "steps", rs.getString("steps")));
            root.appendChild(elem(doc, "createdAt", rs.getString("created_at")));

            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(doc), new StreamResult(new File(fileName)));

            System.out.println("Mentés exportálva: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static Element elem(Document doc, String name, String value) {
        Element e = doc.createElement(name);
        e.appendChild(doc.createTextNode(value));
        return e;
    }
//EXPORT EGYESEVEL XML VÉGE


//MENTÉSEK LISTÁZÁSA
    public static void listSavedGames() {
        String sql = "SELECT id, player_name, created_at FROM saved_games";

        try (Connection c = DBConnectInit.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nID | Játékos | Dátum");
            while (rs.next()) {
                System.out.printf(
                        "%d | %s | %s%n",
                        rs.getInt("id"),
                        rs.getString("player_name"),
                        rs.getString("created_at")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//MENTÉSEK LISTÁZÁSA VÉGE

}
