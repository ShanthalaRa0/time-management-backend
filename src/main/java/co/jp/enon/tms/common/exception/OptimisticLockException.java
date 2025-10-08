package co.jp.enon.tms.common.exception;

// OptimisticLockExceptionはミドルで対応するので（必ずAPで対応が必要ではないため）RuntimeExceptionを継承する。
// また、RuntimeExceptionのみロールバックされるので、Exceptionでロールバックが必要な場合は、
// Serviceクラスのメソッドの@Transactionalアノテーションを次のようにする。
// @Transactional(rollbackFor = Exception.class)
public class OptimisticLockException extends RuntimeException {
	
	private static final long serialVersionUID = 1L; 

    // 引数なしコンストラクタの定義
	public OptimisticLockException() {
	}

	public OptimisticLockException(String msg) {
		this.msg = msg;
	}

	private String msg;

	public String getMessage() {
		return this.msg;
	}
}
