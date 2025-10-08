package co.jp.enon.tms.common.security.dto;

public class SuccessResponse extends Response{

    public Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
