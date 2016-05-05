package http.wh.comhttp.callback;

import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import http.wh.comhttp.error.AppException;
import http.wh.comhttp.entities.IEntity;
import http.wh.comhttp.utils.LogUtils;
import http.wh.comhttp.core.AbstractCallback;

/**
 * Created by wanghao on 16/4/27.
 */
public abstract class JsonReaderCallback<T extends IEntity> extends AbstractCallback<T> {


    @Override
    protected T bindData(String path) throws AppException {
        try {
            LogUtils.e("paht:" + path);
            Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            T t = ((Class<T>) type).newInstance();
            FileReader in = new FileReader(path);
            JsonReader reader = new JsonReader(in);
            String node;
            reader.beginObject();
            while (reader.hasNext()) {
                node = reader.nextName();
                if ("data".equalsIgnoreCase(node)) {
                    t.readFromJson(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return t;
        } catch (Exception e) {
            LogUtils.e("paht:" + e.toString());
            throw new AppException(AppException.ErrorType.JSON, e.getMessage());
        }
    }
}
