package http.wh.comhttp.Builder;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanghao on 16/5/3.
 */
public class ComHttp {

    private static Context context;
    private int connectionTimeout = 15 * 1000;
    private int readTimeout = 15 * 1000;
    private boolean enableProgressUpdated = false;
    private boolean enableProgressDownload = false;
    private int maxReteryCount = 3;
    private Map<String, String> headers;
    private String encoding="UTF-8";
    public static class configHolder {
        public static ComHttp rc = new ComHttp();
    }

    public void ComHttp() {

    }

    public static ComHttp getInstance(Context mContext) {
        context = mContext;
        return getInstance();
    }

    public static ComHttp getInstance() {

        return configHolder.rc;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put(key, value);
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setMaxReteryCount(int maxReteryCount) {
        this.maxReteryCount = maxReteryCount;
    }

    public void setEnableProgressUpdated(boolean enableProgressUpdated) {
        if(enableProgressUpdated){
            enableProgressDownload=false;
        }
        this.enableProgressUpdated = enableProgressUpdated;
    }

    public void setEnableProgressDownload(boolean enableProgressDownload) {
        if(enableProgressDownload){
            enableProgressUpdated=false;
        }
        this.enableProgressDownload = enableProgressDownload;
    }

    public static Context getContext() {
        return context;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public boolean isEnableProgressUpdated() {
        return enableProgressUpdated;
    }

    public boolean isEnableProgressDownload() {
        return enableProgressDownload;
    }

    public int getMaxReteryCount() {
        return maxReteryCount;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
