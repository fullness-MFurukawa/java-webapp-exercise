package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.exception.ServiceException;

/**
 *	デモ用ロ：グインコントローラー
 *	@author Fullness,Inc.
 *	@since 2025-03-13 
 *	@version 1.0.0
 */
@WebServlet("/")
public class AuthenticationController extends HttpServlet  {

	/**
	 * ログイン画面を表示
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(req, resp);
	}
	
	/**
	 * ログイン実行
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<String> loginInfo = InputParameterLogin(req);

		List<String> errMsgs;
		try {
			errMsgs = checkInputData(loginInfo);
		} catch (ServiceException e) {
			resp.sendRedirect("error");
			return;
		}

		if (!errMsgs.isEmpty()) {
			req.setAttribute("errMsgs", errMsgs);
			req.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(req, resp);
			return;
		}
		resp.sendRedirect("menu");
		return;
	}

	/**
	 * パラメータを取得してリストで返却
	 * @param req HTTPリクエスト
	 * @return ログイン情報
	 */
	private List<String> InputParameterLogin(HttpServletRequest req) {
		List<String> loginInfo = new ArrayList<>();
		String userName = req.getParameter("username");
		String password = req.getParameter("password");
		loginInfo.add(userName);
		loginInfo.add(password);
		return loginInfo;
	}

	/**
	 * デモ用：ログイン処理
	 * userName：demo && password：pass
	 *
	 * @param 入力されたログイン情報
	 * @return エラーメッセージのリスト
	 * @throws ServiceException リソースからデータの取得に失敗
	 */
	private List<String> checkInputData(List<String> loginInfo) throws ServiceException {
		List<String> errMsgs = new ArrayList<String>();

		String userName = loginInfo.get(0);
		String password = loginInfo.get(1);
		
		if (userName == "") {
			errMsgs.add("ユーザー名を入力して下さい");
		}
		
		if (password == "") {
			errMsgs.add("パスワードを入力して下さい");
		}
		
		if (userName != "" && password != "") {
			if (!(userName.equals("demo") && password.equals("pass"))) {
				errMsgs.add("認証に不備があります");
			}			
		}

		return errMsgs;
	}

}
