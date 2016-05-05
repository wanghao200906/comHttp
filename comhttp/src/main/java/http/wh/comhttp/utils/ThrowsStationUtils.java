package http.wh.comhttp.utils;

import android.webkit.URLUtil;

import http.wh.comhttp.Builder.ComHttp;
import http.wh.comhttp.Request;
import http.wh.comhttp.error.AppException;

/**
 * Created by wanghao on 16/5/3.
 */
public class ThrowsStationUtils {


    public static void isNotUrl(Request request) throws AppException {

        if (!URLUtil.isNetworkUrl(request.url)) {
            throw new AppException(AppException.ErrorType.MANUAL, "the url :" + request.url + " is not valid");
        }
    }

    public static void noNewWork() throws AppException {
        if (NetStatus.getAPNType(ComHttp.getContext()) == NetStatus.NoNet) {
//            LogUtils.e("etStatus.getAPNType(ComHttp.context):"+NetStatus.getAPNType(ComHttp.context));
            throw new AppException(AppException.ErrorType.NO_NET, "the network can not be userd");
        }
    }
}
