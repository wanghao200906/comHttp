package http.wh.comhttp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanghao on 16/4/29.
 */
public class RequestManager {
    private static RequestManager mInstance;
    private HashMap<String, ArrayList<Request>> mCachedRequest;

    public static RequestManager getInstance() {
        if (mInstance == null) {
            mInstance = new RequestManager();
        }
        return mInstance;
    }

    private RequestManager() {
        mCachedRequest = new HashMap<String, ArrayList<Request>>();
    }

    public void performRequest(Request request) {

        request.execute();
        if (request.tag == null) {
            return;// no need to cache the request
        }
        if (!mCachedRequest.containsKey(request.tag)) {
            ArrayList<Request> requests = new ArrayList<Request>();
            mCachedRequest.put(request.tag, requests);
        }
        mCachedRequest.get(request.tag).add(request);
    }

    public void cancelRequest(String tag) {
        cancelRequest(tag, false);
    }

    public void cancelRequest(String tag, boolean force) {
        if (tag == null || "".equals(tag.trim())) {
            return;
        }
//        TODO find requests by tag, and cancel them
        if (mCachedRequest.containsKey(tag)) {
            ArrayList<Request> requests = mCachedRequest.remove(tag);
            for (Request request : requests) {
                if (!request.isCancelled && tag.equals(request.tag)) {
                    request.cancel(force);
                }
            }
        }

    }

    public void cancelAll() {
        cancelAll(false);
    }

    public void cancelAll(boolean force) {
        for (Map.Entry<String, ArrayList<Request>> entry : mCachedRequest.entrySet()) {
            ArrayList<Request> requests = entry.getValue();
            for (Request request : requests) {
                if (!request.isCancelled) {
                    request.cancel(force);
                }
            }
        }
    }

}
