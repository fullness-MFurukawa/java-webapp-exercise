package model.dto;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;

/**
 *	部門情報DTOクラスの単体テストドライバ
 *	@author Fullness,Inc.
 *	@since 2025-03-12
 *	@version 1.0.0
 */
public class DepartmentTest {
	@Test
	void testConstructorAndGetters() {
		// コンストラクタでオブジェクトを作成
	    Department dept = new Department(1, "営業部");
	    // ゲッターの確認
	    assertEquals(1, dept.getDeptId());
	    assertEquals("営業部", dept.getDeptName());
	}
	@Test
	void testSetters() {
		// デフォルトコンストラクタでオブジェクトを作成
		Department dept = new Department();
	    // セッターで値を設定
	    dept.setDeptId(2);
	    dept.setDeptName("開発部");
	    // 値の確認
	    assertEquals(2, dept.getDeptId());
	    assertEquals("開発部", dept.getDeptName());
	}
	
	@Test
	void testEqualsAndHashCode() {
		// 同じ値を持つオブジェクトを作成
	    Department dept1 = new Department(3, "総務部");
	    Department dept2 = new Department(3, "総務部");
	    // equals() の確認
	    assertEquals(dept1, dept2);
	    // hashCode() の確認
	    assertEquals(dept1.hashCode(), dept2.hashCode());
	}
	
	@Test
    void testNotEquals() {
        Department dept1 = new Department(4, "人事部");
        Department dept2 = new Department(5, "経理部");
        // 異なるオブジェクトの比較
        assertNotEquals(dept1, dept2);
    }
	
	@Test
	void testToString() {
		Department dept = new Department(6, "法務部");
		// toString()がnullではないことを確認
	    assertNotNull(dept.toString());    
	    // toString() の出力形式を確認（Lombok のデフォルト形式）
	    assertTrue(dept.toString().contains("deptId=6"));
	    assertTrue(dept.toString().contains("deptName=法務部"));
	}
}
