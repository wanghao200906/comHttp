package http.wh.comhttp.entities;

import com.google.gson.stream.JsonReader;

import http.wh.comhttp.error.AppException;

public interface IEntity {
    void readFromJson(JsonReader reader) throws AppException;
}
