package com.example.laboras.serializers;

import com.example.laboras.ds.Course;
import com.example.laboras.ds.Folder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class FolderGSONSerializer implements JsonSerializer<Folder> {
    @Override
    public JsonElement serialize(Folder folder, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject folderJson = new JsonObject();
        folderJson.addProperty("folderId", folder.getId());
        folderJson.addProperty("folderTitle", folder.getTitle());
        folderJson.addProperty("parentCourseId", String.valueOf(folder.getParentCourse()));

        return folderJson;
    }
}
