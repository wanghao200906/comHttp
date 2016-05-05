package http.wh.comhttp.core;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import http.wh.comhttp.Request;
import http.wh.comhttp.error.AppException;
import http.wh.comhttp.itf.OnProgressUpdatedListener;
import http.wh.comhttp.upload.UploadUtil;
import http.wh.comhttp.utils.ThrowsStationUtils;
import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;


public class OKHttpUrlConnectionUtil {
    private static OkHttpClient mClient;
    private static final String TAG = "OKHttpUrlConnectionUtil";

    public synchronized static HttpURLConnection execute(Request request, OnProgressUpdatedListener listener) throws AppException {
        ThrowsStationUtils.isNotUrl(request);
        ThrowsStationUtils.noNewWork();
        if (mClient == null) {
            initializeOkHttp();
        }
        switch (request.method) {
            case GET:
            case DELETE:
                return get(request);
            case POST:
            case PUT:
                return post(request, listener);
        }

        return null;
    }

    private static void initializeOkHttp() {
        mClient = new OkHttpClient();
    }


    private static HttpURLConnection get(Request request) throws AppException {


        try {
            request.checkIfCanceled();

            HttpURLConnection connection = new OkUrlFactory(mClient).open(new URL(request.url));
            connection.setRequestMethod(request.method.name());
            connection.setConnectTimeout(request.connectionTimeout);
            connection.setReadTimeout(request.readTimeout);

            addHeader(connection, request.headers);

            request.checkIfCanceled();
            return connection;
        } catch (MalformedURLException e) {
            throw new AppException(AppException.ErrorType.SERVER, e.getMessage());
        } catch (ProtocolException e) {
            throw new AppException(AppException.ErrorType.SERVER, e.getMessage());
        }

    }


    private static HttpURLConnection post(Request request, OnProgressUpdatedListener listener) throws AppException {
        HttpURLConnection connection = null;
        OutputStream os = null;
        try {
            request.checkIfCanceled();
            connection = new OkUrlFactory(mClient).open(new URL(request.url));
            connection.setRequestMethod(request.method.name());
            connection.setConnectTimeout(request.connectionTimeout);
            connection.setReadTimeout(request.readTimeout);
            connection.setDoOutput(true);
            addHeader(connection, request.headers);
            request.checkIfCanceled();
            os = connection.getOutputStream();
            if (request.filePath != null) {
                UploadUtil.upload(os, request.filePath);
            } else if (request.fileEntities != null) {
                UploadUtil.upload(os, request.getBody(), request.fileEntities, listener);
            } else if (request.getBody() != null) {
                os.write(request.getBody().getBytes());
            } else {
                throw new AppException(AppException.ErrorType.MANUAL, "the post request has no post content");
            }
            request.checkIfCanceled();
        } catch (InterruptedIOException e) {
            throw new AppException(AppException.ErrorType.TIMEOUT, e.getMessage());
        } catch (IOException e) {
            throw new AppException(AppException.ErrorType.SERVER, e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                throw new AppException(AppException.ErrorType.IO, "the post outputstream can't be closed");
            }
        }

        return connection;
    }

    private static void addHeader(HttpURLConnection connection, Map<String, String> headers) {
        if (headers == null || headers.size() == 0)
            return;

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }
}
