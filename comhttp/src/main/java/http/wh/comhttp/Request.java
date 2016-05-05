package http.wh.comhttp;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import http.wh.comhttp.Builder.ComHttp;
import http.wh.comhttp.core.RequestTask;
import http.wh.comhttp.entities.FileEntity;
import http.wh.comhttp.error.AppException;
import http.wh.comhttp.itf.ICallback;
import http.wh.comhttp.itf.OnGlobalExceptionListener;

public class Request {
    private ComHttp comHttp = ComHttp.getInstance();
    public volatile boolean isCancelled;
    public String url;
    //    public String content;
    public Map<String, String> headers = comHttp.getHeaders();
    public Map<String, String> bodys;

    public RequestMethod method;
    public RequestTool tool;

    public final int executeComplicat = 1;
    public final int executeSingle = 2;


    public int connectionTimeout = comHttp.getConnectionTimeout();
    public int readTimeout = comHttp.getReadTimeout();
    public ICallback iCallback;


    public boolean enableProgressUpdated = comHttp.isEnableProgressUpdated();
    public boolean enableProgressDownload = comHttp.isEnableProgressDownload();
    public OnGlobalExceptionListener globalExceptionListener;
    public String tag;
    private RequestTask task;
    private int requestType = 0;
    public String filePath;
    public ArrayList<FileEntity> fileEntities;

    public static final int STATE_UPLOAD = 1;
    public static final int STATE_DOWNLOAD = 2;
    private String encoding = comHttp.getEncoding();

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setDownloadStatus(Boolean flag) {
        this.enableProgressDownload = flag;
        if (flag) {

            this.enableProgressUpdated = !flag;
        }

    }

    public void setUpdatedStatus(Boolean flag) {
        this.enableProgressUpdated = flag;
        if (flag) {

            this.enableProgressDownload = !flag;
        }


    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setCallBack(ICallback iCallback) {
        this.iCallback = iCallback;
    }


    public void setGlobalExceptionListener(OnGlobalExceptionListener onGlobalExceptionListener) {
        this.globalExceptionListener = onGlobalExceptionListener;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void cancel(boolean force) {
        isCancelled = true;
        iCallback.cancel();
        if (force && task != null) {
            task.cancel(force);
        }
    }

    public void checkIfCanceled() throws AppException {
        if (isCancelled) {
            throw new AppException(AppException.ErrorType.CANCEL, "the request has benn canceled");
        }
    }

    public void execute() {
        task = new RequestTask(this);
        switch (requestType) {
            case executeComplicat:
                task.executeComplicat();
                break;
            case executeSingle:
                task.executeSingle();
                break;
            default:
                task.executeComplicat();
                break;
        }
    }

    public void setExecuteComplicat() {
        requestType = executeComplicat;
    }

    public void setExecuteSingle() {
        requestType = executeSingle;
    }


    public enum RequestMethod {GET, POST, PUT, DELETE}

    public enum RequestTool {OKHTTP, URLCONNECTION}

    public int maxReteryCount = comHttp.getMaxReteryCount();

    public void setMaxReteryCount(int maxReteryCount) {
        this.maxReteryCount = maxReteryCount;
    }


    public Request(String url, RequestMethod method) {
        this.url = url;
        this.method = method;
        this.tool = RequestTool.URLCONNECTION;

    }

    public Request(String url, RequestMethod method, RequestTool tool) {
        this.url = url;
        this.method = method;
        this.tool = tool;

    }

    public Request(String url) {

        this.url = url;
        this.method = RequestMethod.GET;
        this.tool = RequestTool.URLCONNECTION;
    }

    public Request(String url, RequestTool tool) {

        this.url = url;
        this.method = RequestMethod.GET;
        this.tool = tool;
    }


    public void addHeader(String key, String value) {
//        if (headers == null) {
        headers = new HashMap<String, String>();
//        }
        headers.put(key, value);
    }


    public void addBody(String key, String value) {

        if (bodys == null) {
            bodys = new HashMap<String, String>();
        }
        bodys.put(key, value);
    }

    public String getBody() throws AppException {
        StringBuffer buffer = new StringBuffer();
        if (bodys != null && !bodys.isEmpty() && bodys.size() > 0) {
            for (Map.Entry<String, String> entry : bodys.entrySet()) {
                buffer.append(entry.getKey()).append('=');
                try {
                    buffer.append(URLEncoder.encode(entry.getValue(), encoding));
                } catch (Exception e) {
                    throw new AppException(AppException.ErrorType.UNSUPPORTEDENCOD, e.toString());
                }
                buffer.append('&');
            }
            buffer.deleteCharAt(buffer.length() - 1);
        }

        return buffer.toString();
    }
}
