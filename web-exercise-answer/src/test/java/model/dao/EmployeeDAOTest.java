package model.dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dto.Employee;
import model.util.ConnectionManager;

/**
 *	社員情報DAOクラスの単体テストドライバ
 *	@author Fullness,Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class EmployeeDAOTest {
	private ConnectionManager connectionManager;
    private Connection connection;
    private EmployeeDAO employeeDAO;

    /**
     * 各テストの前処理: `EmployeeDAO` の初期化
     * @throws SQLException
     */
    @BeforeEach
    void setUpBeforeEach() throws SQLException {
        connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection();
        connection.setAutoCommit(false);
        employeeDAO = new EmployeeDAO(connection);
    }
    
    /**
     * 各テストの後処理: `rollback();` でデータ変更をリセット
     * @throws SQLException
     */
    @AfterEach
    void tearDownAfterEach() throws SQLException {
        if (connection != null) {
            connection.rollback(); 
        }
    }
    
    /**
     * selectAllWithDepartment()のテスト
     */
    @Test
    void testSelectAllWithDepartment() throws SQLException {
        var result = employeeDAO.selectAllWithDepartment();
        assertEquals(3, result.size()); // すでに登録済みのデータが3件ある
        assertEquals("山田太郎", result.get(0).getEmpName());
        assertEquals("川田次郎", result.get(1).getEmpName());
        assertEquals("海田三郎", result.get(2).getEmpName());
    }
    
    /**
     * selectByIdWithDepartment()のテスト
     */
    @Test
    void testSelectByIdWithDepartment() throws SQLException {
        Employee result = employeeDAO.selectByIdWithDepartment(1001);
        assertNotNull(result);
        assertEquals("山田太郎", result.getEmpName());
        assertEquals(101, result.getDeptId());
        assertEquals("人事部", result.getDepartment().getDeptName());
    }
    
    /**
     * selectByMailAddress()のテスト
     */
    @Test
    void testSelectByMailAddress() throws SQLException {
        Employee result = employeeDAO.selectByMailAddress("jiro@foo.bar.baz");
        assertNotNull(result);
        assertEquals("川田次郎", result.getEmpName());
        assertEquals(102, result.getDeptId());
    }
    
    /**
     * insert()のテスト
     */
    @Test
    void testInsert() throws SQLException {
        Employee newEmp = new Employee(
        		0, 
        		"佐藤四郎", 
        		"000-4444-5555", 
        		"shiro@foo.bar.baz", 103, null);
        int result = employeeDAO.insert(newEmp);
        assertEquals(1, result);
        Employee insertedEmp = employeeDAO.selectByMailAddress("shiro@foo.bar.baz");
        assertNotNull(insertedEmp);
        assertEquals("佐藤四郎", insertedEmp.getEmpName());
        assertEquals(103, insertedEmp.getDeptId());
    }
    /**
     * update()のテスト
     */
    @Test
    void testUpdate() throws SQLException {
        Employee employee = employeeDAO.selectByIdWithDepartment(1001);
        employee.setEmpName("山田一郎");
        employee.setPhone("000-5555-6666");
        employee.setMailAddress("ichiro@foo.bar.baz");
        employee.setDeptId(102); // 企画部に異動
        int result = employeeDAO.update(employee);
        assertEquals(1, result);
        Employee updatedEmp = employeeDAO.selectByIdWithDepartment(1001);
        assertEquals("山田一郎", updatedEmp.getEmpName());
        assertEquals("000-5555-6666", updatedEmp.getPhone());
        assertEquals("ichiro@foo.bar.baz", updatedEmp.getMailAddress());
        assertEquals(102, updatedEmp.getDeptId());
    }
    
    /**
     * delete()のテスト
     */
    @Test
    void testDelete() throws SQLException {
        Employee employee = employeeDAO.selectByIdWithDepartment(1002);
        int result = employeeDAO.delete(employee);
        assertEquals(1, result);
        Employee deletedEmp = employeeDAO.selectByIdWithDepartment(1002);
        assertNull(deletedEmp);
    }
    
    /**
     * countByDeptId()のテスト
     */
    @Test
    void testCountByDeptId() throws SQLException {
        int countBefore = employeeDAO.countByDeptId(101);
        assertEquals(2, countBefore); // 人事部には2人いる
        Employee newEmp = new Employee(
        		0, 
        		"田中五郎", 
        		"000-7777-8888", 
        		"goro@foo.bar.baz", 101, null);
        employeeDAO.insert(newEmp);
        int countAfter = employeeDAO.countByDeptId(101);
        assertEquals(3, countAfter); // 1人追加されたので3人になる
    }
}
