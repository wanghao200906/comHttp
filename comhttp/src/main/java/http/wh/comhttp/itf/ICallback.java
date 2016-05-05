package http.wh.comhttp.itf;

import java.net.HttpURLConnection;

import http.wh.comhttp.error.AppException;

/**
 * Created by wanghao on 16/4/22.
 */
public interface ICallback<T> {
    void onSuccess(T result);

    void onFailure(AppException exp);

    T invoke(HttpURLConnection connection, OnProgressUpdatedListener listener) throws AppException;

    T invoke(HttpURLConnection connection) throws AppException;

    void onProgressUpdated(int state, int curLen, int totalLen);

    void cancel();

    T postRequest(T t);

    T preRequest();

    T preRequestForUI(T t);
}
