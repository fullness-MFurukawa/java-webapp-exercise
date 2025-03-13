package controller.validator;

import java.util.List;

/**
 *	入力値検証例外クラス
 *	@author Fullness,Inc.
 *	@since 2025-03-12
 *	@version 1.0.0
 */
public class ValidateException extends Exception {
	private final List<String> errorMessages;
	/**
	 * コンストラクタ
	 */
	public ValidateException(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	public List<String> getErrorMessages() {
		return errorMessages;
	}
}
