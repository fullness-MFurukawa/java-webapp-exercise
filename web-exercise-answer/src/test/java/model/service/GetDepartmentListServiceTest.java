package model.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dto.Department;
import model.exception.ServiceException;
import model.util.ConnectionManager;

/**
 *	UC02:部門一覧表示クラスの単体テストドライバ
 *	@author Fullness,Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class GetDepartmentListServiceTest {
	private ConnectionManager connectionManager;
    private Connection connection;
    private GetDepartmentListService getDepartmentListService;
    
    /**
     * 各テストの前処理: `GetDepartmentListService` の初期化
     * @throws SQLException
     */
    @BeforeEach
    void setUpBeforeEach() throws SQLException {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection();
        connection.setAutoCommit(false); 
        getDepartmentListService = new GetDepartmentListService();
    }
    /**
     * 各テストの後処理: `rollback();` でデータ変更をリセット + 削除データを復元
     * @throws SQLException
     */
    @AfterEach
    void tearDownAfterEach() throws SQLException {
        if (connection != null) {
            connection.rollback(); 
        }
        try (var checkStmt = connection.prepareStatement("SELECT COUNT(*) FROM t_department");
                var stmt = connection.createStatement()) {
               
               try (var rs = checkStmt.executeQuery()) {
                   if (rs.next() && rs.getInt(1) == 0) { // データがない場合のみ復元
                       // t_departmentのデータを復元
                       stmt.executeUpdate("""
                           INSERT INTO t_department (dept_id, dept_name) VALUES
                           (101, '人事部'),
                           (102, '企画部'),
                           (103, 'システム開発部')
                       """);

                       // t_employeeのデータを復元
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
     * readDepartmentAll()のテスト（データが存在する場合）
     */
    @Test
    void testReadDepartmentAll_HasData() throws ServiceException {
        List<Department> result = getDepartmentListService.readDepartmentAll();
        // 3件取得できることを確認
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        // 各データの検証
        assertEquals(101, result.get(0).getDeptId());
        assertEquals("人事部", result.get(0).getDeptName());
        assertEquals(102, result.get(1).getDeptId());
        assertEquals("企画部", result.get(1).getDeptName());
        assertEquals(103, result.get(2).getDeptId());
        assertEquals("システム開発部", result.get(2).getDeptName());
    }
    
    /**
     * readDepartmentAll()のテスト（データが存在しない場合）
     */
    @Test
    void testReadDepartmentAll_NoData() throws SQLException, ServiceException {
        // データをすべて削除
        try (var stmt = connection.createStatement()) {
        	stmt.executeUpdate("DELETE FROM t_employee");
            stmt.executeUpdate("DELETE FROM t_department");
            connection.commit();  
        }
        // 空のリストが返ることを確認
        List<Department> result = getDepartmentListService.readDepartmentAll();
        assertNotNull(result);
        assertTrue(result.isEmpty(), "部門データが存在しない場合、空のリストが返るべき");
    }
}
