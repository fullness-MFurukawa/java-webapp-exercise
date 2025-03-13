package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.dto.Employee;
import model.exception.ServiceException;
import model.service.DeleteEmployeeService;

/**
 *	P002【社員一覧画面】削除ボタン 用 コントローラー<br>
 *	URL: /empdeleteselectbutton
 *	@author Fullness,Inc.
 *	@since 2025-03-13 
 *	@version 1.0.0
 */
@WebServlet("/empdeleteselectbutton")
public class EmployeeDeleteSelectButtonServlet  extends HttpServlet {

	/**
	 * 社員一覧画面の削除ボタンから実行され、入力内容をセッションに保存して確認画面にリダイレクト<br>
	 * 入力内容確認のDB処理に失敗した場合はエラー画面にリダイレクト
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);

		Employee targetEmp;
		try {
			int empId = getInputParameterEmpID(req);
			targetEmp = new DeleteEmployeeService().readEmployeeByEmpId(empId);
		} catch (ServiceException e) {
			resp.sendRedirect("error");
			return;
		}

		session.setAttribute("delEmpInput", targetEmp);
		session.setAttribute("empDeletedFlg", false);
		resp.sendRedirect("empdeletecheck");
		return;
	}

	/**
	 * 入力パラメータを取得し社員IDとして返却
	 * @param req HTTPリクエスト
	 * @return 入力パラメータの社員ID
	 */
	private int getInputParameterEmpID(HttpServletRequest req){
		String empId = req.getParameter("empId");
		return Integer.parseInt(empId);
	}
}
