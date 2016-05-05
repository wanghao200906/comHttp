package http.wh.comhttp.callback;

import http.wh.comhttp.core.AbstractCallback;
import http.wh.comhttp.error.AppException;

/**
 * Created by wanghao on 16/4/27.
 */
public abstract class FileCallback extends AbstractCallback<String> {
    @Override
    protected String bindData(String path) throws AppException {
        return path;
    }


}
