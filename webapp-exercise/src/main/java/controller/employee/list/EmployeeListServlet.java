package controller.employee.list;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.exception.ServiceException;
import model.service.GetEmployeeListService;

/**
 *	P002【社員一覧画面】用 コントローラー<br>
 *	URL: /emplist
 *	@author Fullness,Inc.
 *	@since 2025-03-12
 *	@version 1.0.0 
 */
@WebServlet("/employee/list")
public class EmployeeListServlet extends HttpServlet{
	/**
	 * 社員一覧画面を表示<br>
	 * 画面に表示する社員のリストの取得に失敗した場合はエラー画面にリダイレクト
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
	throws ServletException, IOException {
		try {
			req.setAttribute("empAllList", 
					new GetEmployeeListService().readEmployeeAllWithDeptName());
		} catch (ServiceException e) {
			resp.sendRedirect("error");
			return;
		}
		req.getRequestDispatcher("/WEB-INF/views/employee/list/list.jsp")
			.forward(req, resp);
		return;
	}
}
