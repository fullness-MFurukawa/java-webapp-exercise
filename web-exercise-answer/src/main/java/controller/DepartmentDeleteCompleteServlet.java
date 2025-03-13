package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.dto.Department;

/**
 * 	P016【部門削除完了画面】用 コントローラー<br>
 * 	URL: /deptdeletecomp
 *  @author Fullness,Inc.
 *	@since 2025-03-13 
 *	@version 1.0.0
 */
@WebServlet("/deptdeletecomp")
public class DepartmentDeleteCompleteServlet extends HttpServlet {

	/**
	 * 部門削除完了画面を表示<br>
	 * セッションに社員の値が保存されていない場合、エラーメッセージをセッションに保存してメニュー画面にリダイレクト
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(true);

		Department department = (Department) session.getAttribute("delDeptComplete");
		if (department == null) {
			session.setAttribute("illegalOperationMsg", "不正な操作です");
			resp.sendRedirect("menu");
			return;
		}
		session.setAttribute("deptDeletedFlg", true);

		session.removeAttribute("delDeptComplete");
		req.setAttribute("delDeptCompleteViewData", department);
		req.getRequestDispatcher("WEB-INF/jsp/department/delete/departmentdeletecomplete.jsp").forward(req, resp);
		return;
	}
}
