package com.example.laboras.serializers;

import com.example.laboras.ds.Course;
import com.example.laboras.ds.Folder;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class FoldersGSONSerializer implements JsonSerializer<List<Folder>> {
    @Override
    public JsonElement serialize(List<Folder> folders, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Folder.class, new FoldersGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (Folder l : folders) {
            jsonArray.add(parser.toJson(l));
        }
        return jsonArray;
    }
}
