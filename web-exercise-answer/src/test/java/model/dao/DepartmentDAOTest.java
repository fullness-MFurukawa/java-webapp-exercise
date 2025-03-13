package model.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dto.Department;
import model.util.ConnectionManager;

/**
 *	部門情報DAOクラスの単体テストドライバ
 *	@author Fullness,Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class DepartmentDAOTest {
	private ConnectionManager connectionManager;
    private Connection connection;
    private DepartmentDAO departmentDAO;

    /**
     * 各テストの前処理: テストターゲットの初期化
     * @throws SQLException
     */
    @BeforeEach
    void setUpBeforeEach() throws SQLException {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection();
        connection.setAutoCommit(false); 
        departmentDAO = new DepartmentDAO(connection);
    }

    /**
     * 各テストの後処理:ロールバックしてデータ変更をリセット
     * @throws SQLException
     */
    @AfterEach
    void tearDownAfterEach() throws SQLException {
        if (connection != null) {
            connection.rollback(); 
        }
    }
    
    /**
     * selectAll()のテスト
     */
    @Test
    void testSelectAll() throws SQLException {
        var result = departmentDAO.selectAll();
        assertEquals(3, result.size()); // すでに登録済みのデータが3件ある
        assertEquals("人事部", result.get(0).getDeptName());
        assertEquals("企画部", result.get(1).getDeptName());
        assertEquals("システム開発部", result.get(2).getDeptName());
    }
    
    /**
     * selectByDeptId()のテスト
     */
    @Test
    void testSelectByDeptId() throws SQLException {
        Department result = departmentDAO.selectByDeptId(101);
        assertNotNull(result);
        assertEquals("人事部", result.getDeptName());
    }
    
    /**
     * selectByDeptName()のテスト
     */
    @Test
    void testSelectByDeptName() throws SQLException {
        Department result = departmentDAO.selectByDeptName("企画部");
        assertNotNull(result);
        assertEquals(102, result.getDeptId());
    }

    /**
     * insert()のテスト
     */
    @Test
    void testInsert() throws SQLException {
        Department newDept = new Department(0, "マーケティング部");
        int result = departmentDAO.insert(newDept);
        assertEquals(1, result);
        Department insertedDept = departmentDAO.selectByDeptName("マーケティング部");
        assertNotNull(insertedDept);
    }
    
    /**
     * update()のテスト
     */
    @Test
    void testUpdate() throws SQLException {
        Department department = departmentDAO.selectByDeptId(101);
        department.setDeptName("人事・総務部");
        int result = departmentDAO.update(department);
        assertEquals(1, result);
        Department updatedDept = departmentDAO.selectByDeptId(101);
        assertEquals("人事・総務部", updatedDept.getDeptName());
    }
    
    /**
     * delete()のテスト
     */
    @Test
    void testDelete() throws SQLException {
        Department department = departmentDAO.selectByDeptId(103);
        int result = departmentDAO.delete(department);
        assertEquals(1, result);
        Department deletedDept = departmentDAO.selectByDeptId(103);
        assertNull(deletedDept);
    }
}
