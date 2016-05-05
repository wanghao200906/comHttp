package http.wh.whttp;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import http.wh.comhttp.error.AppException;
import http.wh.comhttp.itf.OnGlobalExceptionListener;
import http.wh.comhttp.RequestManager;

/**
 * Created by wanghao on 16/4/29.
 */
public class BaseActivity extends AppCompatActivity implements OnGlobalExceptionListener {
    private static final String TAG = "BaseActivity";

    @Override
    public boolean handlerAppException(AppException e) {
        Log.e(TAG, "handlerAppException: "+e.toString()+"  -e.statusCode-:"+e.statusCode +"-e.type-"+e.type+"-responseMessage-"+e.responseMessage);
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        RequestManager.getInstance().cancelRequest(toString(),true);
    }
}
