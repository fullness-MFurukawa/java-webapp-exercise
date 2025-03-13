package model.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dto.Employee;
import model.exception.ServiceException;
import model.util.ConnectionManager;

/**
 *	UC05:社員情報削除クラスの単体テストドライバ
 *	@author Fullness,Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class DeleteEmployeeServiceTest {
	private ConnectionManager connectionManager;
    private Connection connection;
    private DeleteEmployeeService deleteEmployeeService;
    
    /**
     * 各テストの前処理:テストターゲットの初期化
     * @throws SQLException
     */
    @BeforeEach
    void setUpBeforeEach() throws SQLException {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection();
        connection.setAutoCommit(false); 
        deleteEmployeeService = new DeleteEmployeeService();
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
        try (var stmt = connection.createStatement();
             var checkStmt = 
            connection.prepareStatement("SELECT COUNT(*) FROM t_employee WHERE emp_id = 1001")) {
            // 事前に削除されているか確認
            try (var rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate("""
                        INSERT INTO t_employee (emp_id, emp_name, phone_number, email_address, dept_id)
                        VALUES (1001, '山田太郎', '000-1111-2222', 'taro@foo.bar.baz', 101)
                    """);
                    connection.commit(); 
                }
            }
        }
    }
    
    /**
     * readEmployeeByEmpId()のテスト（社員が存在する場合）
     */
    @Test
    void testReadEmployeeByEmpId_Exists() throws ServiceException {
        Employee result = deleteEmployeeService.readEmployeeByEmpId(1001);
        assertNotNull(result);
        assertEquals(1001, result.getEmpId());
        assertEquals("山田太郎", result.getEmpName());
        assertEquals("000-1111-2222", result.getPhone());
        assertEquals("taro@foo.bar.baz", result.getMailAddress());
        assertEquals(101, result.getDeptId());
    }

    /**
     * readEmployeeByEmpId()のテスト（社員が存在しない場合）
     */
    @Test
    void testReadEmployeeByEmpId_NotExists() {
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            deleteEmployeeService.readEmployeeByEmpId(9999); 
        });
        assertEquals("取得件数が0件でした", exception.getMessage());
    }
    
    /**
     * deleteEmployee()のテスト（社員を削除）
     */
    @Test
    void testDeleteEmployee_Success() throws ServiceException {
        Employee employee = deleteEmployeeService.readEmployeeByEmpId(1001); 
        assertNotNull(employee);
        deleteEmployeeService.deleteEmployee(employee);
        Employee deletedEmp = null;
        try {
            deletedEmp = deleteEmployeeService.readEmployeeByEmpId(1001);
        } catch (ServiceException e) {
            assertEquals("取得件数が0件でした", e.getMessage()); 
        }
        assertNull(deletedEmp, "社員が削除されているため、取得結果はnullであるべき");
    }
    
    /**
     * deleteEmployee()のテスト（存在しない社員を削除しようとする）
     */
    @Test
    void testDeleteEmployee_Fail_NotExists() {
        Employee nonExistentEmployee = new Employee(
        		9999, 
        		"不存在社員", 
        		"000-0000-0000", 
        		"nonexistent@foo.bar.baz", 101, null);
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            deleteEmployeeService.deleteEmployee(nonExistentEmployee); 
        });
        assertEquals("削除対象の特定に失敗しました", exception.getMessage());
    }
}
