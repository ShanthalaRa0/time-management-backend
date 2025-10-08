package co.jp.enon.tms.common.exception;

// LogicalExceptionはミドルで対応するので（必ずAPで対応が必要ではないため）RuntimeExceptionを継承する。
// また、RuntimeExceptionのみロールバックされるので、Exceptionでロールバックが必要な場合は、
// Serviceクラスのメソッドの@Transactionalアノテーションを次のようにする。
// @Transactional(rollbackFor = Exception.class)
public class LogicalException extends RuntimeException {
	
	private static final long serialVersionUID = 1L; 

    // 引数なしコンストラクタの定義
	public LogicalException() {
	}
	public LogicalException(String code, String message) {
		this.code = code;
		this.message = message;
	}

	private String code;
	private String message;

	public String getCode() {
		return this.code;
	}
	public String getMessage() {
		return this.message;
	}
}
