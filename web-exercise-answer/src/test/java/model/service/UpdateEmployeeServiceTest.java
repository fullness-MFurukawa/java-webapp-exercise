package model.service;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
 *	UC07:社員情報更新クラスの単体テストドライバ
 *	@author Fullness,Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class UpdateEmployeeServiceTest {
	private UpdateEmployeeService updateEmployeeService;

    /**
     * 各テストの前処理: テストターゲットの初期化
     */
    @BeforeEach
    void setUpBeforeEach() {
        updateEmployeeService = new UpdateEmployeeService();
    }
    /**
     * 各テストの後処理: `UPDATE` を明示的に実行してデータを復元
     */
    @AfterEach
    void tearDownAfterEach() throws SQLException {
        try (Connection cleanupConnection = new ConnectionManager().getConnection();
             PreparedStatement stmt = cleanupConnection.prepareStatement("""
                 UPDATE t_employee SET 
                     emp_name = '山田太郎',
                     phone_number = '000-1111-2222',
                     email_address = 'taro@foo.bar.baz',
                     dept_id = 101
                 WHERE emp_id = 1001
             """)) {
            stmt.executeUpdate();
            cleanupConnection.commit();
        }
    }
    
    /**
     * isDuplicateMailAddress()のテスト（既存メールアドレスの場合:true）
     */
    @Test
    void testIsDuplicateMailAddress_True() throws ServiceException {
        boolean result = updateEmployeeService.isDuplicateMailAddress(1001, "jiro@foo.bar.baz"); // 既存のメールアドレス
        assertTrue(result, "既存のメールアドレスは `true` であるべき");
    }

    /**
     * isDuplicateMailAddress()のテスト（新規メールアドレスの場合:false）
     */
    @Test
    void testIsDuplicateMailAddress_False() throws ServiceException {
        boolean result = updateEmployeeService.isDuplicateMailAddress(1001, "newemail@foo.bar.baz"); // 存在しないメールアドレス
        assertFalse(result, "新規メールアドレスは `false` であるべき");
    }

    /**
     * readEmployeeByEmpId()のテスト（既存の社員ID）
     */
    @Test
    void testReadEmployeeByEmpId_Success() throws ServiceException {
        Employee employee = updateEmployeeService.readEmployeeByEmpId(1001);
        assertNotNull(employee);
        assertEquals(1001, employee.getEmpId());
        assertEquals("山田太郎", employee.getEmpName());
    }

    /**
     * readEmployeeByEmpId()のテスト（存在しない社員ID）
     */
    @Test
    void testReadEmployeeByEmpId_NotFound() {
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            updateEmployeeService.readEmployeeByEmpId(9999);
        });
        assertEquals("取得件数が0件でした", exception.getMessage());
    }
    
    /**
     * readDepartmentAll()のテスト（すべての部門情報を取得できるか）
     */
    @Test
    void testReadDepartmentAll_Success() throws ServiceException {
        List<Department> departments = updateEmployeeService.readDepartmentAll();
        assertNotNull(departments);
        assertFalse(departments.isEmpty());
    }

    /**
     * readDepartmentByDeptId()のテスト（既存の部門ID）
     */
    @Test
    void testReadDepartmentByDeptId_Success() throws ServiceException {
        Department department = updateEmployeeService.readDepartmentByDeptId(101);
        assertNotNull(department);
        assertEquals(101, department.getDeptId());
    }

    /**
     * readDepartmentByDeptId()のテスト（存在しない部門ID）
     */
    @Test
    void testReadDepartmentByDeptId_NotFound() {
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            updateEmployeeService.readDepartmentByDeptId(9999);
        });
        assertEquals("取得件数が0件でした", exception.getMessage());
    }
    
    /**
     * updateEmployee()のテスト（正常に社員情報を更新できるか）
     */
    @Test
    void testUpdateEmployee_Success() throws ServiceException {
        // 更新前のデータ取得
        Employee beforeUpdate = updateEmployeeService.readEmployeeByEmpId(1001);
        assertNotNull(beforeUpdate);
        assertEquals("山田太郎", beforeUpdate.getEmpName(), "更新前の社員名が '山田太郎' であること");
        // 更新処理
        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmpId(1001);
        updatedEmployee.setEmpName("山田新太郎");
        updatedEmployee.setPhone("000-9999-8888");
        updatedEmployee.setMailAddress("shintaro@foo.bar.baz");
        updatedEmployee.setDeptId(102); // 企画部に変更
        updateEmployeeService.updateEmployee(updatedEmployee);
        // 更新後のデータ取得
        Employee afterUpdate = updateEmployeeService.readEmployeeByEmpId(1001);
        assertNotNull(afterUpdate);
        assertEquals("山田新太郎", afterUpdate.getEmpName(), "更新後の社員名が '山田新太郎' であること");
        assertEquals("000-9999-8888", afterUpdate.getPhone(), "更新後の電話番号が '000-9999-8888' であること");
        assertEquals("shintaro@foo.bar.baz", afterUpdate.getMailAddress(), "更新後のメールが 'shintaro@foo.bar.baz' であること");
        assertEquals(102, afterUpdate.getDeptId(), "更新後の部門IDが '102' (企画部) であること");
    }
}
