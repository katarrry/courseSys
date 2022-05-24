package com.example.laboras.serializers;

import com.example.laboras.ds.Course;
import com.example.laboras.ds.File;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class FileGSONSerializer implements JsonSerializer<File> {
    @Override
    public JsonElement serialize(File file, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject fileJson = new JsonObject();
        fileJson.addProperty("fileId", file.getId());
        fileJson.addProperty("fileTitle", file.getTitle());
        fileJson.addProperty("parentFolderId", String.valueOf(file.getParentFolder()));

        return fileJson;
    }
}
