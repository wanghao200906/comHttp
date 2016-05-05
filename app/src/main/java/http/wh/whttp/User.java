package http.wh.whttp;


import com.google.gson.stream.JsonReader;

import java.io.IOException;

import http.wh.comhttp.error.AppException;
import http.wh.comhttp.entities.IEntity;

public class User  implements IEntity{
    //    {"ret":200,"data":{"id":"2","account":"stay4it","email":"stay4it@163.com","username":"stay","password":"123456","avatar":"","token":"lI7oi96+8Q\/TIib9dSpy3mj7maC\/6ZEfI3HdwT\/ZwQI="},"msg":""}
    public String id;
    public String account;
    public String email;
    public String username;
    public String token;

    @Override
    public String toString() {
        return username + " : " + email;
    }


    @Override
    public void readFromJson(JsonReader reader) throws AppException {
        try {
            reader.beginObject();
            String node ;
            while (reader.hasNext()) {
                node = reader.nextName();
                if ("username".equalsIgnoreCase(node)) {
                    username = reader.nextString();
                } else if ("email".equalsIgnoreCase(node)) {
                    email = reader.nextString();
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
