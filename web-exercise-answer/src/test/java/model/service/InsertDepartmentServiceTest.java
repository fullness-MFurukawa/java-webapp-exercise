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
 *	UC04:部門情報登録クラスの単体テストドライバ
 *	@author Fullness, Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class InsertDepartmentServiceTest {
	private ConnectionManager connectionManager;
    private Connection connection;
    private InsertDepartmentService insertDepartmentService;

    /**
     * 各テストの前処理: テストターゲットの初期化
     * @throws SQLException
     */
    @BeforeEach
    void setUpBeforeEach() throws SQLException {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection();
        connection.setAutoCommit(false); 
        insertDepartmentService = new InsertDepartmentService();
    }

    /**
     * 各テストの後処理: データ変更をリセット
     * @throws SQLException
     */
    @AfterEach
    void tearDownAfterEach() throws SQLException {
        if (connection != null) {
            connection.rollback(); 
        }
        // マーケティング部が登録されている場合のみ削除
        try (var checkStmt = connection.prepareStatement("SELECT COUNT(*) FROM t_department WHERE dept_name = ?");
             var deleteStmt = 
            connection.prepareStatement("DELETE FROM t_department WHERE dept_name = ?")) {

            checkStmt.setString(1, "マーケティング部");

            try (var rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) { 
                    deleteStmt.setString(1, "マーケティング部");
                    deleteStmt.executeUpdate();
                    connection.commit(); 
                }
            }
        }
    }
    
    /**
     * isDuplicateDeptName()のテスト（既存の部門名）
     */
    @Test
    void testIsDuplicateDeptName_Existing() throws ServiceException {
        assertTrue(insertDepartmentService.isDuplicateDeptName("人事部"),
                   "既存の部門名 '人事部' は重複と判定されるべき");
    }

    /**
     * isDuplicateDeptName()のテスト（存在しない部門名）
     */
    @Test
    void testIsDuplicateDeptName_New() throws ServiceException {
        assertFalse(insertDepartmentService.isDuplicateDeptName("マーケティング部"),
                    "存在しない部門名 'マーケティング部' は重複と判定されるべきでない");
    }
    
    /**
     * createDepartment()のテスト（正常に登録できるか）
     */
    @Test
    void testCreateDepartment_Success() throws ServiceException {
        Department newDepartment = new Department();
        newDepartment.setDeptName("マーケティング部");
        // 新しい部門を登録
        insertDepartmentService.createDepartment(newDepartment);
        // 登録後にisDuplicateDeptName()の結果がtrueになるか確認
        assertTrue(insertDepartmentService.isDuplicateDeptName("マーケティング部"),
                   "新規追加した 'マーケティング部' は重複と判定されるべき");
    }
}
