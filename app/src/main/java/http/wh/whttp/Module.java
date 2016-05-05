package http.wh.whttp;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

import http.wh.comhttp.error.AppException;
import http.wh.comhttp.entities.IEntity;

public class Module implements IEntity {
    public String name;
    public long timestamp;

    @Override
    public void readFromJson(JsonReader reader) throws AppException {
        try {
            reader.beginObject();
            String node ;
            while (reader.hasNext()) {
                node = reader.nextName();
                if ("name".equalsIgnoreCase(node)) {
                    name = reader.nextString();
                } else if ("timestamp".equalsIgnoreCase(node)) {
                    timestamp = reader.nextLong();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            throw new AppException(AppException.ErrorType.JSON, e.getMessage());
        }
    }
}
