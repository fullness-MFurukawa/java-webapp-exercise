package model.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部門情報DTOクラス
 * @author Fullness,Inc.
 * @since 2025-03-11
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
	/** 部門ID */
	private int deptId;
	/** 部門名 */
	private String deptName;

	/**
	 * ハッシュコードを生成する
	 */
	@Override
	public int hashCode() {
		return Objects.hash(deptName, deptId);
	}
	/**
	 * インスタンスの等価性を検証する
	 * @param obj 比較するインスタンス
	 * @return true:同一　false:同一でない
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Department other = (Department) obj;
		return Objects.equals(deptName, other.deptName) && deptId == other.deptId;
	}
}
