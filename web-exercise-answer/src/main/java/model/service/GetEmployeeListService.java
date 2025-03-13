package model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.dao.EmployeeDAO;
import model.dto.Employee;
import model.exception.ServiceException;
import model.util.ConnectionManager;

/**
 *	UC01:社員一覧表示クラス<br>
 *	ユーザがシステムに登録されている全社員を取得(一覧表示機能)
 *	@author Fullness,Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class GetEmployeeListService {

	/**
	 * リソースに登録されている全ての社員情報とその社員が所属する部門名を取得<br>
	 * 社員情報が存在しない場合は空のリストを返却
	 *
	 * @return リソースに登録されている全ての社員情報とその社員が所属する部門名のリスト
	 * @throws ServiceException リソースの接続に失敗、又は、情報の取得に失敗
	 */
	public List<Employee> readEmployeeAllWithDeptName() throws ServiceException {
		ConnectionManager c = new ConnectionManager();
		List<Employee> empList;
		try (Connection connection = c.getConnection();) {
			try {
				EmployeeDAO dao = new EmployeeDAO(connection);
				empList = dao.selectAllWithDepartment();
			} catch (SQLException e) {
				throw new ServiceException("取得失敗", e);
			}
		} catch (SQLException e) {
			throw new ServiceException("DB処理エラー", e);
		}
		return empList;
	}
}