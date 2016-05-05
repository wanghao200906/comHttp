package http.wh.whttp;

import android.test.InstrumentationTestCase;
import android.util.Log;

import http.wh.comhttp.core.HttpUrlConnectionUtil;
import http.wh.comhttp.itf.ICallback;
import http.wh.comhttp.Request;

public class TestHttp extends InstrumentationTestCase {
    private static final String TAG = "TestHttp";

    public void testHttpGet() throws Throwable {
        String url = "http://api.stay4it.com";
        Request request = new Request(url);
        String result = HttpUrlConnectionUtil.execute(request);
        Log.e("stay", "testHttpGet return:" + result);
    }

    public void testHttpPost() throws Throwable {
        String url = "http://api.stay4it.com/v1/public/core/?service=user.login";
        String content = "account=stay4it&password=123456";
        Request request = new Request(url, Request.RequestMethod.POST);
        request.content = content;
        String result = HttpUrlConnectionUtil.execute(request);
        Log.e("stay", "testHttpGet return:" + result);
    }

    public void testHttpPosttoSub() throws Throwable {
        String url = "http://api.stay4it.com/v1/public/core/?service=user.login";
        String content = "account=stay4it&password=123456";
        Request request = new Request(url, Request.RequestMethod.POST);
        request.setCallBack(new ICallback() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
            }

            @Override
            public void onFailure(Exception exp) {
                Log.e(TAG, "onFailure: " + exp.toString());
            }
        });
        request.content = content;
        String result = HttpUrlConnectionUtil.execute(request);
        Log.e("stay", "testHttpGet return:" + result);
    }
}
