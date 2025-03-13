package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.dto.Employee;

/**
 *	P014【社員更新完了画面】用 コントローラー<br>
 *	URL: /empupdatecomp
 *	@author Fullness,Inc.
 *	@since 2025-03-13 
 *	@version 1.0.0
 */
@WebServlet("/empupdatecomp")
public class EmployeeUpdateCompleteServlet extends HttpServlet{
	/**
	 * 社員更新完了画面を表示<br>
	 * セッションに部門の値が保存されていない場合はエラーメッセージをセッションに保存してメニュー画面にリダイレクト
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);

		Employee employee = (Employee) session.getAttribute("updEmpComplete");
		if (employee == null) {
			session.setAttribute("illegalOperationMsg", "不正な操作です");
			resp.sendRedirect("menu");
			return;
		}
		session.setAttribute("empUpdatedFlg", true);

		session.removeAttribute("updEmpComplete");
		req.setAttribute("updEmpCompleteViewData", employee);
		req.getRequestDispatcher("WEB-INF/jsp/employee/update/employeeupdatecomplete.jsp").forward(req, resp);
		return;
	}
}
