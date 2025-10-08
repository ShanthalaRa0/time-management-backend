package co.jp.enon.tms.common.security.dto;

public class ErrorStatus extends Status {

    private String message;

    public ErrorStatus(String message) {
        super(Status.CODE_ERROR);
        this.message = message;
    }
    public ErrorStatus(String code, String message) {
        super(code);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
