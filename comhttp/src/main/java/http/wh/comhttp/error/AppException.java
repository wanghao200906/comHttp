package http.wh.comhttp.error;

/**
 * Created by wanghao on 16/4/28.
 */
public class AppException extends Exception {


    public int statusCode;
    public String responseMessage;

    public enum ErrorType {
        TIMEOUT,//链接超时
        SERVER,//服务器异常 unHostExcepition
        JSON, //json异常
        IO,
        MANUAL, //配置异常
        CANCEL, //访问被取消了
        UPLOAD,
        NO_NET,//没有网络
        UNSUPPORTEDENCOD,//post 请求格式 错误
        FILE_NOT_FOUND
    }

    public ErrorType type;

    public AppException(int status, String responseMessage) {
        super(responseMessage);
        this.type = ErrorType.SERVER;
        this.statusCode = status;
        this.responseMessage = responseMessage;
    }

    public AppException(ErrorType type, String detailMessage) {
        super(detailMessage);
        this.type = type;
    }
}
