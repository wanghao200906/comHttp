package http.wh.comhttp.core;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import http.wh.comhttp.error.AppException;
import http.wh.comhttp.itf.ICallback;
import http.wh.comhttp.itf.OnProgressUpdatedListener;

/**
 * Created by wanghao on 16/4/27.
 */
public abstract class AbstractCallback<T> implements ICallback<T> {

    private static final String TAG = "AbstractCallback";
    private String path;
    private volatile boolean isCancled;

    @Override
    public T invoke(HttpURLConnection connection) throws AppException {
        return invoke(connection, null);
    }

    @Override
    public T invoke(HttpURLConnection connection, OnProgressUpdatedListener listener) throws AppException {
        try {
            checkIfCanceled();
            InputStream is = null;
            int status = connection.getResponseCode();
            Log.e(TAG, "status: " + status);
            if (status == connection.HTTP_OK) {

//                support gzip || deflate
                String encoding = connection.getContentEncoding();
                if (encoding != null && "gzip".equalsIgnoreCase(encoding))
                    is = new GZIPInputStream(connection.getInputStream());
                else if (encoding != null
                        && "deflate".equalsIgnoreCase(encoding))
                    is = new InflaterInputStream(connection.getInputStream());
                else {
                    is = connection.getInputStream();
                }


                if (path == null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        checkIfCanceled();
                        out.write(buffer, 0, len);
                    }
                    is.close();
                    out.flush();
                    out.close();
                    String result = new String(out.toByteArray());
                    T t = bindData(result);
                    return postRequest(t);
                } else {
                    FileOutputStream out = new FileOutputStream(path);

                    int totalLen = connection.getContentLength();
                    int curLen = 0;
                    byte[] buffer = new byte[2048];
                    int len;
                    int percent = 0;
                    while ((len = is.read(buffer)) != -1) {
                        checkIfCanceled();
                        out.write(buffer, 0, len);
                        if (curLen * 100l / totalLen > percent) {
                            curLen += len;
                            if (listener != null) {
                                listener.onProgressUpdated(curLen, totalLen);
                            }
                            percent = (int) (curLen * 100l / totalLen);
                        }
                    }
                    is.close();
                    out.flush();
                    out.close();
                    T t = bindData(path);
                    return postRequest(t);
                }
            } else {
                Log.e(TAG, "invoke: " + status);
                throw new AppException(status, connection.getResponseMessage());
            }
        } catch (SocketTimeoutException e) {
            throw new AppException(AppException.ErrorType.TIMEOUT, e.toString());
        } catch (InterruptedIOException e) {
            throw new AppException(AppException.ErrorType.TIMEOUT, e.toString());
        } catch (Exception e) {
            Log.e(TAG, "invoke try catch: " + e.toString());
            throw new AppException(AppException.ErrorType.SERVER, e.toString());
        }
    }

    /**
     * 子线程运行,网络请求有返回值了,但还没返回到到onSuccess
     *
     * @param t
     * @return
     */
    @Override
    public T postRequest(T t) {
        return t;
    }

    /**
     * 子线程运行,网络请求最开始
     *
     * @return
     */
    @Override
    public T preRequest() {
        return null;

    }

    /**
     * preRequest执行完 回调到主线程
     *
     * @param t
     * @return
     */
    @Override
    public T preRequestForUI(T t) {
        return null;
    }

    protected void checkIfCanceled() throws AppException {
        if (isCancled) {
            throw new AppException(AppException.ErrorType.CANCEL, "the request has benn canceled");
        }

    }

    @Override
    public void cancel() {
        isCancled = true;
    }

    @Override
    public void onProgressUpdated(int status, int cur, int total) {

    }

    protected abstract T bindData(String result) throws Exception;


    public ICallback setCatchPath(String path) {
        this.path = path;
        return this;
    }
}
