package org.example.amoeba.db;

import java.sql.Connection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectInitTest {

    @BeforeAll
    static void setup() {
        DBConnectInit.setDbUrl("jdbc:sqlite::memory:");
        DBConnectInit.init();
    }

    @Test
    void testGetConnection() throws Exception {
        try (Connection conn = DBConnectInit.getConnection()) {
            assertNotNull(conn);
            assertFalse(conn.isClosed());
        }
    }

    @Test
    void testInitDoesNotThrow() {
        assertDoesNotThrow(DBConnectInit::init);
    }
}
