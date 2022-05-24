package com.example.laboras.serializers;

import com.example.laboras.ds.Course;
import com.example.laboras.ds.File;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class FilesGSONSerializer implements JsonSerializer<List<File>> {
    @Override
    public JsonElement serialize(List<File> files, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(File.class, new FilesGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (File l : files) {
            jsonArray.add(parser.toJson(l));
        }
        return jsonArray;
    }
}
