package controller.department.register;

import java.io.IOException;
import java.util.Arrays;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import controller.validator.DepartmentValidator;
import controller.validator.ValidateException;
import model.dto.Department;
import model.exception.ServiceException;
import model.service.InsertDepartmentService;

/**
 *	部門登録コントローラーが利用するヘルパクラス
 *	@author Fullness, Inc.
 *	@sine 2025-03-12
 *	@version 1.0.0
 */
public class DepartmentRegisterHelper {
	// 部門用リクエストパラメータ検証
	private final DepartmentValidator validator;
	// 部門登録サービス
	private final InsertDepartmentService service;
	/**
	 * コンストラクタ
	 */
	public DepartmentRegisterHelper() {
		this.service = new InsertDepartmentService();
		this.validator = new DepartmentValidator();
	}
	
	public void entry(HttpServletRequest request , HttpServletResponse response) 
			throws IOException, ServletException{
		// P007:部門登録入力画面の表示
		request.getRequestDispatcher(
				"/WEB-INF/views/department/register/entry.jsp")
				.forward(request, response);
	}
	
	public void reEntry(HttpServletRequest request ,HttpServletResponse response) 
	throws IOException{
		// 入力画面にリダイレクトする
		response.sendRedirect(
			request.getContextPath()+"/department/register?action=entry");
	}
	
	public void registerRedirect(HttpServletRequest request ,HttpServletResponse response)
	throws IOException{
		// PRGパターン: GETリクエストに変更する
		response.sendRedirect(
			request.getContextPath()+"/department/register?action=register");
	}
	
	public void register(HttpServletRequest request ,HttpServletResponse response)
	throws IOException, ServletException{
		var session = request.getSession();
		// セッションに新しい部署を格納する
		var department = (Department) session.getAttribute("newDepartment");
		try {
			service.createDepartment(department);
			request.getRequestDispatcher(
					"/WEB-INF/views/department/register/complete.jsp")
					.forward(request, response);
			request.setAttribute("newDeptName", department.getDeptName());
		}catch(ServiceException e) {
			// サービスクラスの例外をキャッチしたのでエラー画面にリダイレクトする
			response.sendRedirect(request.getContextPath()+"/error");
		}
	}
	
	public void confirm(HttpServletRequest request , HttpServletResponse response) 
	throws IOException, ServletException{
		var session = request.getSession();
		// リクエストパラメータをDTOに格納する
		var department = mappingRequestParameter(request);
		try {
			// 入力された部門データの検証する
			validator.validate(department);
			// 同一部署が登録済みかを確認する
			if (service.isDuplicateDeptName(department.getDeptName())) {
				var errorMessage = 
					Arrays.asList("この部門名は既に登録されています。別の部門名で登録してください。");
				throw new ValidateException(errorMessage);
			}
		}catch(ValidateException e) {
			// セッションにエラーメッセージを格納する
			session.setAttribute("errorMessages", e.getErrorMessages());
			// 入力画面にリダイレクトする
			response.sendRedirect(
					request.getContextPath()+"/department/register");
			return;
		}	
		catch(ServiceException e) {
			// サービスクラスの例外をキャッチしたのでエラー画面にリダイレクトする
			response.sendRedirect(request.getContextPath()+"/error");
			return;
		}
		// セッションに新しい部署を格納する
		session.setAttribute("newDepartment", department);
		// 確認画面を表示する
		request.getRequestDispatcher(
			"/WEB-INF/views/department/register/confirm.jsp")
			.forward(request, response);
	}
	/**
	 * リクエストパラメータをDTOに格納して返す
	 * @param req HttpServletRequest
	 */
	private Department mappingRequestParameter(HttpServletRequest request) {
		String deptName = request.getParameter("name");
		Department department = new Department();
		department.setDeptName(deptName);
		return department;
	}
}
