package http.wh.comhttp.callback;

import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import http.wh.comhttp.core.AbstractCallback;
import http.wh.comhttp.error.AppException;
import http.wh.comhttp.entities.IEntity;

public abstract class JsonArrayReaderCallback<T extends IEntity> extends AbstractCallback<ArrayList<T>> {
    @Override
    protected ArrayList<T> bindData(String path) throws AppException {
        try {


            Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            ArrayList<T> ts = new ArrayList<T>();
            T t;

//            StringReader in = new StringReader(data.toString());
            FileReader in = new FileReader(path);
            JsonReader reader = new JsonReader(in);
            String node;
            reader.beginObject();
            while (reader.hasNext()) {
                node = reader.nextName();
                if ("data".equalsIgnoreCase(node)) {
                    reader.beginArray();
                    while (reader.hasNext()) {
                        t = ((Class<T>) type).newInstance();
                        t.readFromJson(reader);
                        ts.add(t);
                    }
                    reader.endArray();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return ts;

        } catch (Exception e) {
            throw new AppException(AppException.ErrorType.JSON, e.getMessage());
        }
    }
}
