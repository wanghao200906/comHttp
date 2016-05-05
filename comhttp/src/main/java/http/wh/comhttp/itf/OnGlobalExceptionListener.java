package http.wh.comhttp.itf;

import http.wh.comhttp.error.AppException;

/**
 * Created by wanghao on 16/4/29.
 */
public interface OnGlobalExceptionListener {
    boolean handlerAppException(AppException exp);
}
