package co.jp.enon.tms.common.exception;

// DeadLockExceptionはミドルで対応するので（必ずAPで対応が必要ではないため）RuntimeExceptionを継承する。
// また、RuntimeExceptionのみロールバックされるので、Exceptionでロールバックが必要な場合は、
// Serviceクラスのメソッドの@Transactionalアノテーションを次のようにする。
// @Transactional(rollbackFor = Exception.class)
public class DeadLockException extends RuntimeException {
	
	private static final long serialVersionUID = 1L; 

    // 引数なしコンストラクタの定義
	public DeadLockException() {
	}

	public DeadLockException(String msg) {
		this.msg = msg;
	}

	private String msg;

	public String getMessage() {
		return this.msg;
	}
}
