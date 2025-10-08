package co.jp.enon.tms.common.exception;

// NotFoundItemExceptionはミドルで対応するので（必ずAPで対応が必要ではないため）RuntimeExceptionを継承する。
// また、RuntimeExceptionのみロールバックされるので、Exceptionでロールバックが必要な場合は、
// Serviceクラスのメソッドの@Transactionalアノテーションを次のようにする。
// @Transactional(rollbackFor = Exception.class)
public class NotFoundItemException extends RuntimeException {
	
	private static final long serialVersionUID = 1L; 

    public NotFoundItemException(String message) {
        super(message);
    }
}
