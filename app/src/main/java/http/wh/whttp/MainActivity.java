package http.wh.whttp;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import http.wh.comhttp.Request;
import http.wh.comhttp.RequestManager;
import http.wh.comhttp.callback.StringCallback;
import http.wh.comhttp.error.AppException;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_http).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    test();
//                    test2();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }


        });
    }

    private void test2() {
        String url = "http://wthrcdn.etouch.cn/WeatherApi?city=北京";
        String path = Environment.getExternalStorageDirectory() + File.separator + "json.tmp";
        Request request = new Request(url, Request.RequestMethod.GET, Request.RequestTool.OKHTTP);
        request.setCallBack(new StringCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
            }

            @Override
            public void onFailure(AppException e) {
                Log.e(TAG, "onFailure: " + e.toString() + "  -e.statusCode-:" + e.statusCode + "-e.type-" + e.type);
            }
        });
        request.setGlobalExceptionListener(this);
        RequestManager.getInstance().performRequest(request);
    }

    private void test() {

        String url = "http://wthrcdn.etouch.cn/WeatherApi?";
        String path = Environment.getExternalStorageDirectory() + File.separator + "json.tmp";
        Request request = new Request(url, Request.RequestMethod.POST, Request.RequestTool.OKHTTP);
        request.setCallBack(new StringCallback() {
//             子线程 得到返回值 和 onsuccess之间进行
            @Override
            public String postRequest(String s) {
                return "nidayede";
            }
//            子线程  进行网络请求之前进行
            @Override
            public String preRequest() {
                return "WOLEGEQU";
            }
//            主线程  在网络请求之前进行请求 后 更新ui
            @Override
            public String preRequestForUI(String s) {
                Toast.makeText(MainActivity.this,s+"", Toast.LENGTH_LONG).show();
                return super.preRequestForUI(s);
            }
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "onSuccess: " + result);
            }
            @Override
            public void onFailure(AppException e) {
                Log.e(TAG, "onFailure: " + e.toString() + "  -e.statusCode-:" + e.statusCode + "-e.type-" + e.type);
            }
        });
        request.addBody("city", "北京");
        request.setGlobalExceptionListener(this);
        RequestManager.getInstance().performRequest(request);
    }


    @Override
    protected void onStop() {
        super.onStop();

    }


}
