package model.service;

import java.sql.Connection;
import java.sql.SQLException;

import model.dao.DepartmentDAO;
import model.dto.Department;
import model.exception.ServiceException;
import model.util.ConnectionManager;

/**
 *	UC04【部門情報登録】機能のクラス<br>
 *	ユーザがシステムに部門情報を新規に登録できるように、部門情報の入力と登録を行う為の機能
 *	@author Fullness,Inc.
 *	@since 2025-03-11
 *	@version 1.0.0
 */
public class InsertDepartmentService {
	/**
	 * 部門名がリソースに登録済のものと被っているかどうかを検証し返却
	 * @param deptName 検証する部門名
	 * @return true:被っている false:被っていない
	 * @throws ServiceException リソースの接続に失敗、又は、情報の取得に失敗
	 */
	public boolean isDuplicateDeptName(String deptName) throws ServiceException {
		ConnectionManager c = new ConnectionManager();
		Department department = null;
		try(Connection connection= c.getConnection();) {
			try {
				DepartmentDAO dao = new DepartmentDAO(connection);
				department = dao.selectByDeptName(deptName);
			} catch (SQLException e) {
				throw new ServiceException("データの取得ができませでした。", e);
			}
		} catch (SQLException e) {
			throw new ServiceException("データベース接続ができません。", e);
		}
		return department != null;
	}
	/**
	 * 部門IDを新規に発行し引数の部門情報と合わせてリソースに登録。更新件数を返却
	 * @param department 部門情報
	 * @throws ServiceException リソースの接続に失敗、又は、情報の更新に失敗
	 */
	public void createDepartment(Department department) throws ServiceException {
		ConnectionManager c = new ConnectionManager();
		try(Connection connection= c.getConnection();) {
			try {
				DepartmentDAO dao = new DepartmentDAO(connection);
				dao.insert(department);
				c.commit();
			} catch (SQLException e) {
				c.rollback();
				throw new ServiceException("部門登録失敗:ロールバックしました。", e);
			}
		} catch (SQLException e) {
			throw new ServiceException("データベース接続ができません。", e);
		}
	}
}
