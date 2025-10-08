package co.jp.enon.tms.common.security.dto;

public class Status {

    public static String CODE_OK = "ok";
    public static String CODE_ERROR = "error";

    private String code;

    public Status(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
