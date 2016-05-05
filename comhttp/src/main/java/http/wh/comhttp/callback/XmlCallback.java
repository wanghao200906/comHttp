package http.wh.comhttp.callback;

import http.wh.comhttp.core.AbstractCallback;

/**
 * Created by wanghao on 16/4/27.
 */
public abstract class XmlCallback<T> extends AbstractCallback<T> {
    @Override
    protected T bindData(String result) throws Exception {
        return null;
    }


}
