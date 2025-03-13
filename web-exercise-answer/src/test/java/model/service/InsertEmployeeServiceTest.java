package model.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dto.Department;
import model.dto.Employee;
import model.exception.ServiceException;
import model.util.ConnectionManager;

/**
 *	UC03:社員情報登録クラスの単体テストドライバ
 *	@author Fullness, Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class InsertEmployeeServiceTest {
	private ConnectionManager connectionManager;
    private Connection connection;
    private InsertEmployeeService insertEmployeeService;

    /**
     * 各テストの前処理: テストターゲットの初期化
     * @throws SQLException
     */
    @BeforeEach
    void setUpBeforeEach() throws SQLException {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection();
        connection.setAutoCommit(false); 
        insertEmployeeService = new InsertEmployeeService();
    }
    /**
     * 各テストの後処理: データ変更のリセッと追加データを削除
     * @throws SQLException
     */
    @AfterEach
    void tearDownAfterEach() throws SQLException {
        if (connection != null) {
            connection.rollback(); 
        }
        // t_employeeにテスト用の社員データにtest_user@company.comがあれば削除
        try (var checkStmt = connection.prepareStatement("SELECT COUNT(*) FROM t_employee WHERE email_address = ?");
             var deleteStmt = connection.prepareStatement("DELETE FROM t_employee WHERE email_address = ?")) {
            checkStmt.setString(1, "test_user@company.com");
            try (var rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) { 
                    deleteStmt.setString(1, "test_user@company.com");
                    deleteStmt.executeUpdate();
                    connection.commit(); 
                }
            }
        }
    }
    
    /**
     * isDuplicateMailAddress()のテスト（既存のメールアドレス）
     */
    @Test
    void testIsDuplicateMailAddress_Existing() throws ServiceException {
        assertTrue(insertEmployeeService.isDuplicateMailAddress("taro@foo.bar.baz"),
                   "既存のメールアドレス 'taro@foo.bar.baz' は重複と判定されるべき");
    }
    /**
     * isDuplicateMailAddress()のテスト（存在しないメールアドレス）
     */
    @Test
    void testIsDuplicateMailAddress_New() throws ServiceException {
        assertFalse(insertEmployeeService.isDuplicateMailAddress("new_user@company.com"),
                    "存在しないメールアドレス 'new_user@company.com' は重複と判定されるべきでない");
    }
    /**
     * readDepartmentAll()のテスト（データが存在する場合）
     */
    @Test
    void testReadDepartmentAll_HasData() throws ServiceException {
        List<Department> result = insertEmployeeService.readDepartmentAll();
        //　データが取得できることを確認
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
    /**
     * readDepartmentByDeptId()のテスト（存在する部門ID）
     */
    @Test
    void testReadDepartmentByDeptId_Existing() throws ServiceException {
        Department department = insertEmployeeService.readDepartmentByDeptId(101);
        // 101`(人事部)が取得できることを確認
        assertNotNull(department);
        assertEquals(101, department.getDeptId());
        assertEquals("人事部", department.getDeptName());
    }
    /**
     * readDepartmentByDeptId()のテスト（存在しない部門ID）
     */
    @Test
    void testReadDepartmentByDeptId_NotFound() {
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            insertEmployeeService.readDepartmentByDeptId(9999); // 存在しない部門ID
        });
        assertEquals("取得件数が0件でした", exception.getMessage());
    }
    /**
     * `createEmployee()` のテスト（正常に登録できるか）
     */
    @Test
    void testCreateEmployee_Success() throws ServiceException {
        Employee newEmployee = new Employee();
        newEmployee.setEmpName("テストユーザー");
        newEmployee.setPhone("000-9999-8888");
        newEmployee.setMailAddress("test_user@company.com");
        newEmployee.setDeptId(101); //　部門ID 101 (人事部)

        // 新しい社員を登録
        insertEmployeeService.createEmployee(newEmployee);
        // 登録後にisDuplicateMailAddress()の結果がtrueになるか確認
        assertTrue(insertEmployeeService.isDuplicateMailAddress("test_user@company.com"),
                   "新規追加した 'test_user@company.com' は重複と判定されるべき");
    }
}
