package model.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dto.Department;
import model.exception.ServiceException;
import model.util.ConnectionManager;

/**
 *	UC08:部門情報更新クラスの単体テストドライバ
 *	@author Fullness, Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class UpdateDepartmentServiceTest {
	private UpdateDepartmentService updateDepartmentService;

	/**
	 * 各テストの前処理: テストターゲットの初期化
	 * @throws SQLException
	 */
	@BeforeEach
	void setUpBeforeEach() throws SQLException {
	    updateDepartmentService = new UpdateDepartmentService();
	}
	/**
     * 各テストの後処理: データ変更のリセットの変更データを復元
     * @throws SQLException
     */
    @AfterEach
    void tearDownAfterEach() throws SQLException {
        try (Connection cleanupConnection = new ConnectionManager().getConnection();
        	PreparedStatement stmt = cleanupConnection.prepareStatement(
        		"UPDATE t_department SET dept_name = '企画部' WHERE dept_id = 102")) {
               stmt.executeUpdate();
               cleanupConnection.commit(); 
        	}
	}

    /**
     * isDuplicateDeptName()のテスト（既存の部門名）
     */
    @Test
    void testIsDuplicateDeptName_Existing() throws ServiceException {
        assertTrue(updateDepartmentService.isDuplicateDeptName(102, "人事部"),
                   "既存の部門名 '人事部' は重複と判定されるべき");
    }
    /**
     * isDuplicateDeptName()のテスト（自身の部門名を変更しない場合）
     */
    @Test
    void testIsDuplicateDeptName_SameDept() throws ServiceException {
        assertFalse(updateDepartmentService.isDuplicateDeptName(101, "人事部"),
                    "自身の部門名を変更しない場合、重複と判定されるべきでない");
    }
    /**
     * isDuplicateDeptName()のテスト（存在しない部門名）
     */
    @Test
    void testIsDuplicateDeptName_NewName() throws ServiceException {
        assertFalse(updateDepartmentService.isDuplicateDeptName(102, "新規部門"),
                    "新規部門 '新規部門' は重複と判定されるべきでない");
    }
    /**
     * readDepartmentByDeptId()のテスト（存在する部門ID）
     */
    @Test
    void testReadDepartmentByDeptId_Existing() throws ServiceException {
        Department department = updateDepartmentService.readDepartmentByDeptId(101);
        // 101(人事部) が取得できることを確認
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
            updateDepartmentService.readDepartmentByDeptId(9999); // 存在しない部門ID
        });
        assertEquals("取得件数が0件でした", exception.getMessage());
    }
    /**
     * updateDepartment()のテスト（正常に部門名を更新できるか）
     */
    @Test
    void testUpdateDepartment_Success() throws ServiceException,SQLException {
        // 更新前のデータ取得
        Department beforeUpdate = updateDepartmentService.readDepartmentByDeptId(102);
        assertNotNull(beforeUpdate);
        assertEquals("企画部", beforeUpdate.getDeptName()); // 更新前の名前
        // 更新処理
        Department updatedDepartment = new Department();
        updatedDepartment.setDeptId(102);
        updatedDepartment.setDeptName("マーケティング部");

        updateDepartmentService.updateDepartment(updatedDepartment);

        // 更新後のデータ取得
        Department afterUpdate = updateDepartmentService.readDepartmentByDeptId(102);
        assertNotNull(afterUpdate);
        assertEquals("マーケティング部", afterUpdate.getDeptName()); // 更新後の名前をチェック
    }
}
