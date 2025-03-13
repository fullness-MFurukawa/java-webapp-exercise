package model.exception;
/**
 *	サービスクラスでスローされる例外クラス
 *	@author Fullness, Inc.
 *	@since 2025-03-13
 *	@version 1.0.0
 */
public class ServiceException extends Exception{
	
	/**
	 * エラーメッセージを設定するコンストラクタ
	 * @param message エラーメッセージ
	 */
	public ServiceException(String message) {
        super(message);
    }
	
	/**
	 * 原因となる例外とエラーメッセージを設定するコンストラクタ
	 * @param message エラーメッセージ
	 * @param cause 原因となる例外
	 */
	public ServiceException(String message, Throwable cause) {
        super(message,cause);
    }
}
