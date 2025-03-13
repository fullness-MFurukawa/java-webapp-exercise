package model.util;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * データベースの接続・切断・トランザクション処理クラスの単体テストドライバ
 * @author Fullness,Inc.
 * @since 2025-03-12
 * @version 1.0.0
 */
public class ConnectionManagerTest {

    private static ConnectionManager connectionManager;
    private Connection connection;

    /**
     * すべてのテストの前に `test_table` を作成
     * @throws SQLException
     */
    @BeforeAll
    static void setUpBeforeAll() throws SQLException {
        connectionManager = new ConnectionManager();
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS test_table (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100)
                );
            """);
            conn.commit(); 
        }
    }

    /**
     * 各テストの前処理: データのみ削除
     * @throws SQLException
     */
    @BeforeEach
    void setUpBeforeEach() throws SQLException {
        connection = connectionManager.getConnection(); 
        connection.setAutoCommit(false); 

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("TRUNCATE TABLE test_table RESTART IDENTITY;");
        }
    }

    /**
     * 各テストの後処理: `rollback();` でトランザクションをリセット
     * @throws SQLException
     */
    @AfterEach
    void tearDownAfterEach() throws SQLException {
        if (connection != null) {
            connection.rollback(); 
        }
    }

    /**
     * すべてのテストの後に `test_table` を削除
     * @throws SQLException
     */
    @AfterAll
    static void tearDownAfterAll() throws SQLException {
        try (Connection conn = connectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS test_table;");
            conn.commit(); 
        }
    }

    @Test
    void testGetConnection() throws SQLException {
        assertNotNull(connection);
        assertFalse(connection.isClosed()); 
    }

    @Test
    void testCommit() throws SQLException {
        Connection conn = connectionManager.getConnection();

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO test_table (name) VALUES ('Test User')");
        }
        conn.commit(); 
        // コミット後にデータが反映されているか確認
        try (Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM test_table")) {
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1)); 
        }
    }

    @Test
    void testRollback() throws SQLException {
        Connection conn = connectionManager.getConnection(); 

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO test_table (name) VALUES ('Rollback User')");
        }
        conn.rollback(); 
        // ロールバック後にデータが存在しないことを確認
        try (Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM test_table")) {
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1)); 
        }
    }
}
