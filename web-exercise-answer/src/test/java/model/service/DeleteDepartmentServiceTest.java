package model.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dto.Department;
import model.exception.ServiceException;
import model.util.ConnectionManager;

/**
 *	UC06:部門情報削除クラスの単体テストドライバ
 *	@author Fullness,Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class DeleteDepartmentServiceTest {
	private ConnectionManager connectionManager;
    private Connection connection;
    private DeleteDepartmentService deleteDepartmentService;

    /**
     * 各テストの前処理: テストターゲットの初期化
     * @throws SQLException
     */
    @BeforeEach
    void setUpBeforeEach() throws SQLException {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection();
        connection.setAutoCommit(false);
        deleteDepartmentService = new DeleteDepartmentService();
    }
    /**
     * 各テストの後処理: ロールバックしてデータ変更をリセット
     * @throws SQLException
     */
    @AfterEach
    void tearDownAfterEach() throws SQLException {
        if (connection != null) {
            connection.rollback(); 
        }
        try (var stmt = connection.createStatement();
        	var checkStmt = 
        	connection.prepareStatement("SELECT COUNT(*) FROM t_department WHERE dept_id = 103")) {
               
            // 事前に削除されているか確認
        	try (var rs = checkStmt.executeQuery()) {
        		if (rs.next() && rs.getInt(1) == 0) {
        			stmt.executeUpdate("""
                    INSERT INTO t_department (dept_id, dept_name)
                    VALUES (103, 'システム開発部')
                    """);
                   connection.commit(); // ✅ 復元を確定
                }
            }
        }
    }
    
    /**
     * isEmployeeExist()のテスト
     */
    @Test
    void testIsEmployeeExist_WithEmployees() throws ServiceException {
        assertTrue(deleteDepartmentService.isEmployeeExist(101)); 	//	101 (人事部) には社員がいる
        assertFalse(deleteDepartmentService.isEmployeeExist(103));	//	103 (システム開発部) は社員なし
    }
    
    /**
     * readDepartmentByDeptId()のテスト（部門が存在する場合）
     */
    @Test
    void testReadDepartmentByDeptId_Exists() throws ServiceException {
        Department result = deleteDepartmentService.readDepartmentByDeptId(102);
        assertNotNull(result);
        assertEquals(102, result.getDeptId());
        assertEquals("企画部", result.getDeptName());
    }

    /**
     * readDepartmentByDeptId()のテスト（部門が存在しない場合）
     */
    @Test
    void testReadDepartmentByDeptId_NotExists() {
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            deleteDepartmentService.readDepartmentByDeptId(999); // 存在しない部門ID
        });
        assertEquals("取得件数が0件でした", exception.getMessage());
    }
    
    /**
     * deleteDepartment()のテスト（社員がいない部門を削除）
     */
    @Test
    void testDeleteDepartment_Success() throws ServiceException {
        Department department = deleteDepartmentService.readDepartmentByDeptId(103);
        assertNotNull(department);
        deleteDepartmentService.deleteDepartment(department);
        Department deletedDept = null;
        try {
        	deletedDept = deleteDepartmentService.readDepartmentByDeptId(103);
        } catch (ServiceException e) {
            assertEquals("取得件数が0件でした", e.getMessage()); 
            assertNull(deletedDept);
        }
    }
    
    /**
     * deleteDepartment()のテスト（社員がいる部門の削除）
     */
    @Test
    void testDeleteDepartment_Fail_EmployeeExists() {
        Department department = new Department(101, "人事部"); // 101 (人事部) には社員がいる

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            deleteDepartmentService.deleteDepartment(department); // 削除不可のはず
        });
        assertEquals("更新失敗。ロールバックしました", exception.getMessage());
    }
}
