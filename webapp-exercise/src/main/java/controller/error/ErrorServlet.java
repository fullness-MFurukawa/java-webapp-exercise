package controller.error;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * P099【システムエラー画面】用 コントローラー<br>
 * URL: /error
 * @author Fullness, Inc.
 * @since 2025-03-11
 * @version 1.0.0
 */
@WebServlet("/error")
public class ErrorServlet extends HttpServlet {
	
	/**
	 * error.jspへフォワードする
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/error.jsp")
		.forward(request, response);
	}

}
