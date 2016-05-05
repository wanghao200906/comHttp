package http.wh.whttp;

import android.app.Application;

import http.wh.comhttp.Builder.ComHttp;

/**
 * Created by wanghao on 16/5/3.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ComHttp instance = ComHttp.getInstance(this);
//        instance.setConnectionTimeout(20000);
//        instance.setMaxReteryCount(3);
//        instance.setReadTimeout(20000);
    }
}
