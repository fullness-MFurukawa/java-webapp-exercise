package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.dto.Department;
import model.exception.ServiceException;
import model.service.DeleteDepartmentService;

/**
 * 	P003【部門一覧画面】削除ボタン 用コントローラー<br>
 * 	URL: /deptdeleteselectbutton
 * 	@author Fullness,Inc.
 *	@since 2025-03-13 
 *	@version 1.0.0
 */
@WebServlet("/deptdeleteselectbutton")
public class DepartmentDeleteSelectButtonServlet  extends HttpServlet {

	/**
	 * 部門一覧画面の削除ボタンから実行され、入力内容をセッションに保存して確認画面にリダイレクト<br>
	 * 入力内容確認のDB処理に失敗した場合はエラー画面にリダイレクト
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);

		Department targetDept;
		try {
			int deptId = getInputParameterDeptID(req);
			targetDept = new DeleteDepartmentService().readDepartmentByDeptId(deptId);
		} catch (ServiceException e) {
			resp.sendRedirect("error");
			return;
		}

		session.setAttribute("delDeptInput", targetDept);
		session.setAttribute("deptDeletedFlg", false);
		resp.sendRedirect("deptdeletecheck");
		return;
	}

	/**
	 * 入力パラメータを取得し部門IDとして返却
	 * @param req HTTPリクエスト
	 * @return 入力パラメータの部門ID
	 */
	private int getInputParameterDeptID(HttpServletRequest req){
		String deptId = req.getParameter("deptId");
		return Integer.parseInt(deptId);
	}
}
