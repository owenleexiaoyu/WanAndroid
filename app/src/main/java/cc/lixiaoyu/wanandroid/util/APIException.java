package cc.lixiaoyu.wanandroid.util;

public class APIException extends Exception {

    private int errorCode;
    private String errorMsg;

    public APIException(){
        super();
    }

    public APIException(int code){
        super();
    }

    public APIException(int code, String msg){
        super(msg);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
