package model.dto;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
/**
 *	社員情報DTOクラスの単体ストドライバ
 *	@author Fullness, Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class EmployeeTest {
	@Test
    void testConstructorAndGetters() {
        // 部門情報の作成
        Department department = new Department(1, "営業部");
        // Employeeオブジェクトの作成
        Employee emp = new Employee(
        		1001, 
        		"山田 太郎", 
        		"090-1234-5678", 
        		"yamada@example.com", 
        		1, department);
        // ゲッターの確認
        assertEquals(1001, emp.getEmpId());
        assertEquals("山田 太郎", emp.getEmpName());
        assertEquals("090-1234-5678", emp.getPhone());
        assertEquals("yamada@example.com", emp.getMailAddress());
        assertEquals(1, emp.getDeptId());
        assertEquals(department, emp.getDepartment());
    }
	
	@Test
    void testSetters() {
        // Employeeオブジェクトを作成
        Employee emp = new Employee();
        // 値を設定
        emp.setEmpId(1002);
        emp.setEmpName("佐藤 花子");
        emp.setPhone("080-9876-5432");
        emp.setMailAddress("sato@example.com");
        emp.setDeptId(2);
        emp.setDepartment(new Department(2, "開発部"));
        // 値の確認
        assertEquals(1002, emp.getEmpId());
        assertEquals("佐藤 花子", emp.getEmpName());
        assertEquals("080-9876-5432", emp.getPhone());
        assertEquals("sato@example.com", emp.getMailAddress());
        assertEquals(2, emp.getDeptId());
        assertEquals(new Department(2, "開発部"), emp.getDepartment());
    }
	@Test
    void testEqualsAndHashCode() {
        // 同じデータのオブジェクト
        Department dept = new Department(3, "人事部");
        Employee emp1 = new Employee(
        		1003, 
        		"田中 一郎",
        		"070-5555-5555",
        		"tanaka@example.com", 3, dept);
        Employee emp2 = new Employee(
        		1003, 
        		"田中 一郎",
        		"070-5555-5555",
        		"tanaka@example.com", 3, dept);
        // equals() の確認
        assertEquals(emp1, emp2);
        // hashCode() の確認
        assertEquals(emp1.hashCode(), emp2.hashCode());
    }
	@Test
    void testNotEquals() {
        Employee emp1 = new Employee(
        		1004,
        		"高橋 二郎", 
        		"070-6666-6666",
        		"takahashi@example.com",
        		4, new Department(4, "経理部"));
        Employee emp2 = new Employee(
        		1005,
        		"鈴木 三郎",
        		"070-7777-7777",
        		"suzuki@example.com",
        		5, new Department(5, "法務部"));
        // 異なるオブジェクトの比較
        assertNotEquals(emp1, emp2);
    }
	@Test
    void testToString() {
        Employee emp = new Employee(
        		1006,
        		"中村 四郎",
        		"070-8888-8888",
        		"nakamura@example.com",
        		6, new Department(6, "総務部"));
        // toString() が null ではないことを確認
        assertNotNull(emp.toString());
        // toString() の出力が正しい形式であることを確認
        assertTrue(emp.toString().contains("empId=1006"));
        assertTrue(emp.toString().contains("empName=中村 四郎"));
        assertTrue(emp.toString().contains("phone=070-8888-8888"));
        assertTrue(emp.toString().contains("mailAddress=nakamura@example.com"));
        assertTrue(emp.toString().contains("deptId=6"));
        assertTrue(emp.toString().contains("department=Department(deptId=6, deptName=総務部)"));
    }
}
