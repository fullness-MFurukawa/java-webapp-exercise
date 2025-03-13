package controller.department.register;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *	部門登録コントローラー
 *	URL: /department/register
 *	@author Fullness, Inc.
 *	@sine 2025-03-12
 *	@version 1.0.0
 */
@WebServlet("/department/register")
public class DepartmentRegisterServlet extends HttpServlet {
	// 部門登録コントローラーが利用するヘルパ
	private final DepartmentRegisterHelper helper;
	/**
	 * コンストラクタ
	 * @param helper  部門登録コントローラーが利用するヘルパ
	 */
	public DepartmentRegisterServlet() {
		this.helper = new DepartmentRegisterHelper();
	}
	
	/**
	 * HTTP GETリクエストへの応答
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String action = request.getParameter("action"); // 具体的なリクエスト内容を取得する
		// 入力画面を要求
		if (action == null || action.equals("entry")) {
			// 入力画面に遷移する
			helper.entry(request, response);
		}
		// PRGパターン 部門の登録
		if (action.equals("register")) {
			// データベースに部門を登録して完了画面に遷移する
			helper.register(request, response);
		}
	}
	
	/**
	 * HTTP POSTリクエストへの応答
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException {
		String action = request.getParameter("action");// 具体的なリクエスト内容を取得する
		// 入力画面で[確認]ボタンがクリックされた
		if (action.equals("confirm")) {
			// 確認画面遷移処理
			helper.confirm(request, response);
		}
		// 確認画面で[再入力]ボタンがクリックされた
		if (action.equals("reEntry")) {
			helper.reEntry(request, response);
		}
		// 確認画面で[登録]ボタンがクリックされた
		if (action.equals("register")) {
			helper.registerRedirect(request, response);
		}
    }
}
