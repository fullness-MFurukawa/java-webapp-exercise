package controller.validator;

import java.util.ArrayList;
import java.util.List;

import model.dto.Department;

/**
 * 部門バリデーションクラス
 * @author Fullness, Inc.
 * @since 2025-03-12
 * @version 1.0.0
 */
public class DepartmentValidator {
	/**
	 * 部門データの入力値検証
	 * @param department
	 * @return エラーメッセージのリスト
	 * @throws ValidateException
	 */
	public void validate(Department department) throws ValidateException{
		List<String> errMsgs = new ArrayList<String>();
		String name = department.getDeptName();
		if (name == "") {
			errMsgs.add("部門名を入力して下さい");
		} else if (name.length() > 100) {
			errMsgs.add("部門名は100文字以内で入力して下さい");
		} 
		if (! errMsgs.isEmpty()) {
			throw new ValidateException(errMsgs);
		}
	}
	/*
	public List<String> validate(Department department) throws ServiceException{
		List<String> errMsgs = new ArrayList<String>();
		String name = department.getDeptName();
		if (name == "") {
			errMsgs.add("部門名を入力して下さい");
		} else if (name.length() > 100) {
			errMsgs.add("部門名は100文字以内で入力して下さい");
		} else if (new InsertDepartmentService().isDuplicateDeptName(name)) {
			errMsgs.add("この部門名は既に登録されています。別の部門名で登録してください。");
		}
		return errMsgs;
	}
	*/
}
