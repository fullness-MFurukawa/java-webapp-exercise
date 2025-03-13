package model.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dto.Employee;
import model.exception.ServiceException;
import model.util.ConnectionManager;

/**
 *	UC01:社員一覧表示クラスの単体テストドライバ
 *	@author Fullness,Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class GetEmployeeListServiceTest {
	private ConnectionManager connectionManager;
    private Connection connection;
    private GetEmployeeListService getEmployeeListService;

    /**
     * 各テストの前処理: テストターゲットの初期化
     * @throws SQLException
     */
    @BeforeEach
    void setUpBeforeEach() throws SQLException {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection();
        connection.setAutoCommit(false); 
        getEmployeeListService = new GetEmployeeListService();
    }

    /**
     * 各テストの後処理:データ変更のリセットと削除データを復元
     * @throws SQLException
     */
    @AfterEach
    void tearDownAfterEach() throws SQLException {
        if (connection != null) {
            connection.rollback(); 
        }
        try (var checkStmt = 
        		connection.prepareStatement("SELECT COUNT(*) FROM t_employee");
             var stmt = connection.createStatement()) {
            
            try (var rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) { // データがない場合のみ復元
                    stmt.executeUpdate("""
                        INSERT INTO t_employee (emp_id, emp_name, phone_number, email_address, dept_id) VALUES
                        (1001, '山田太郎', '000-1111-2222', 'taro@foo.bar.baz', 101),
                        (1002, '川田次郎', '000-2222-3333', 'jiro@foo.bar.baz', 102),
                        (1003, '海田三郎', '000-3333-4444', 'saburo@foo.bar.baz', 101)
                    """);

                    connection.commit(); 
                }
            }
        }
    }

    /**
     * readEmployeeAllWithDeptName()のテスト（データが存在する場合）
     */
    @Test
    void testReadEmployeeAllWithDeptName_HasData() throws ServiceException {
        List<Employee> result = getEmployeeListService.readEmployeeAllWithDeptName();
        // データが3件取得できることを確認
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        // 各データの検証
        assertEquals(1001, result.get(0).getEmpId());
        assertEquals("山田太郎", result.get(0).getEmpName());
        assertEquals("人事部", result.get(0).getDepartment().getDeptName());
        assertEquals(1002, result.get(1).getEmpId());
        assertEquals("川田次郎", result.get(1).getEmpName());
        assertEquals("企画部", result.get(1).getDepartment().getDeptName());
        assertEquals(1003, result.get(2).getEmpId());
        assertEquals("海田三郎", result.get(2).getEmpName());
        assertEquals("人事部", result.get(2).getDepartment().getDeptName());
    }

    /**
     * readEmployeeAllWithDeptName()のテスト（データが存在しない場合）
     */
    @Test
    void testReadEmployeeAllWithDeptName_NoData() throws SQLException, ServiceException {
        // データをすべて削除
        try (var stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM t_employee");
            connection.commit();
        }
        // データがない場合に空のリストが返ることを確認
        List<Employee> result = getEmployeeListService.readEmployeeAllWithDeptName();
        assertNotNull(result);
        assertTrue(result.isEmpty(), "社員データが存在しない場合、空のリストが返るべき");
    }
}
