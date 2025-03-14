package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
/**
 * P001【メニュー画面】用 コントローラー<br>
 * URL: /menu
 * @author Fullness,Inc.
 * @since 2025-03-11
 * @version 1.0.0
 */
@WebServlet("/menu")
public class MenuServlet extends HttpServlet {
	/**
	 * メニュー画面を表示する
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
	throws ServletException, IOException {
		HttpSession session = req.getSession(false); // セッションが存在しない場合は作成しない
        if (session != null) {
            String illegalOperationMsg = (String)session.getAttribute("illegalOperationMsg");
            // セッションをすべてクリアする
            session.invalidate();
            // 再度セッションを取得（新しいセッションを作成）
            session = req.getSession(true);
            if (illegalOperationMsg != null) {
                session.setAttribute("illegalOperationMsg", illegalOperationMsg);
            }
        }
		req.getRequestDispatcher("/WEB-INF/views/menu.jsp").forward(req, resp);
	}
}
