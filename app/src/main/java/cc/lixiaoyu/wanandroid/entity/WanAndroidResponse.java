package cc.lixiaoyu.wanandroid.entity;

import cc.lixiaoyu.wanandroid.util.network.APIException;

/**
 * WANAndroid返回数据最外层的实体类
 *
 * @param <T>
 */
public class WanAndroidResponse<T> {
    private int errorCode;
    private String errorMsg;
    private T data;

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

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return errorCode == 0;
    }

    public T getData() throws APIException {
        if (isSuccess()) {
            return data;
        } else {
            throw new APIException(errorCode, errorMsg);
        }
    }

    public Optional<T> transform() {
        return new Optional<>(data);
    }
}
