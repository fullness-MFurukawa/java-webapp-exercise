package controller.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpFilter;


/**
 * リクエストパラメータをUTF-8にエンコーディングするフィルタ
 * @author Fullness, Inc.
 * @since 2025-03-11
 * @version 1.0.0
 */
@WebFilter(
		urlPatterns = { "/*" }, 
		initParams = { @WebInitParam(name = "encoding", value = "UTF-8") })
public class ExerciseFilter extends HttpFilter implements Filter {
       
	//	エンコード文字コード:UTF-8
	private String encoding;

	/**
	 * 終了処理
	 */
	public void destroy() {
	}
	
	/**
	 * 初期処理
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// @WebInitParamのvalue属性値を取得する
		encoding = fConfig.getInitParameter("encoding");
	}

	/**
	 * フィルタチェーン
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// リクエストパラメータの文字コードをUTF-8にエンコーディングする
		request.setCharacterEncoding(encoding);
		// リクエスト処理をチェーンされたServletに委譲する
		chain.doFilter(request, response);
	}

}
