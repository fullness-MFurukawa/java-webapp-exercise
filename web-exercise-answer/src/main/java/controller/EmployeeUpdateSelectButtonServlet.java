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
import model.service.UpdateEmployeeService;

/**
 *	P002【社員一覧画面】更新ボタン 用コントローラー<br>
 *	URL: /empupdateselectbutton
 *	@author Fullness,Inc.
 *	@since 2025-03-13 
 *	@version 1.0.0
 */
@WebServlet("/empupdateselectbutton")
public class EmployeeUpdateSelectButtonServlet extends HttpServlet {
	/**
	 * 社員一覧画面の更新ボタンから実行され、入力内容をセッションに保存して確認画面にリダイレクト<br>
	 * 入力内容確認のDB処理に失敗した場合はエラー画面にリダイレクト
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);

		Employee targetEmp;
		try {
			int empId = getInputParameterEmpID(req);
			targetEmp = new UpdateEmployeeService().readEmployeeByEmpId(empId);
		} catch (ServiceException e) {
			resp.sendRedirect("error");
			return;
		}

		session.setAttribute("updEmpInput", targetEmp);
		session.setAttribute("empUpdatedFlg", false);
		resp.sendRedirect("empupdateinput");
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
