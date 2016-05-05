package http.wh.comhttp.core;

import java.net.HttpURLConnection;

import http.wh.comhttp.error.AppException;
import http.wh.comhttp.utils.MultiAsynctask;
import http.wh.comhttp.itf.OnProgressUpdatedListener;
import http.wh.comhttp.Request;

/**
 * Created by wanghao on 16/4/22.
 */
public class RequestTask extends MultiAsynctask<Void, Integer, Object> {
    private static final String TAG = "RequestTask";
    private Request request;

    public RequestTask(Request request) {
        this.request = request;
    }

    @Override
    protected Object doInBackground(Void... params) {
        if (request.iCallback != null) {
            final Object o = request.iCallback.preRequest();
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (o != null) {
                        request.iCallback.preRequestForUI(o);
                    }
                }
            });

        }
        return request(0);
    }

    public Object request(int retry) {
        try {
//                FIXME: for HttpUrlConnection
            HttpURLConnection connection = null;
            if (request.tool == Request.RequestTool.URLCONNECTION) {
                connection = HttpUrlConnectionUtil.execute(request, !request.enableProgressDownload ? null : new OnProgressUpdatedListener() {
                    @Override
                    public void onProgressUpdated(int curLen, int totalLen) {
                        publishProgress(Request.STATE_UPLOAD, curLen, totalLen);
                    }
                });
            } else {
//                FIXME : for OkHttpUrlConnection request
                connection = OKHttpUrlConnectionUtil.execute(request, !request.enableProgressDownload ? null : new OnProgressUpdatedListener() {
                    @Override
                    public void onProgressUpdated(int curLen, int totalLen) {
                        publishProgress(Request.STATE_UPLOAD, curLen, totalLen);
                    }
                });
            }


            if (request.enableProgressUpdated) {
                return request.iCallback.invoke(connection, new OnProgressUpdatedListener() {
                    @Override
                    public void onProgressUpdated(int curLen, int totalLen) {
                        publishProgress(Request.STATE_DOWNLOAD, curLen, totalLen);
                    }
                });
            } else if (request.enableProgressDownload) {
                return request.iCallback.invoke(connection, new OnProgressUpdatedListener() {
                    @Override
                    public void onProgressUpdated(int curLen, int totalLen) {
                        publishProgress(Request.STATE_UPLOAD, curLen, totalLen);
                    }
                });
            } else {
                return request.iCallback.invoke(connection);
            }
        } catch (AppException e) {
            if (e.type == AppException.ErrorType.TIMEOUT) {
                if (retry < request.maxReteryCount) {
                    retry++;
                    return request(retry);
                }
            }
            return e;
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        request.iCallback.onProgressUpdated(values[0], values[1], values[2]);

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o instanceof AppException) {
            if (request.globalExceptionListener != null) {
                if (!request.globalExceptionListener.handlerAppException((AppException) o)) {
                    request.iCallback.onFailure((AppException) o);
                }
            } else {
                request.iCallback.onFailure((AppException) o);

            }
        } else {
            request.iCallback.onSuccess(o);
        }
    }
}
