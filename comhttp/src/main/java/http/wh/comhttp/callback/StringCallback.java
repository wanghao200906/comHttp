package http.wh.comhttp.callback;

import http.wh.comhttp.core.AbstractCallback;

/**
 * Created by wanghao on 16/4/27.
 */
public abstract class StringCallback  extends AbstractCallback<String> {
    @Override
    protected String bindData(String result) throws Exception {
        return result;
    }


}
