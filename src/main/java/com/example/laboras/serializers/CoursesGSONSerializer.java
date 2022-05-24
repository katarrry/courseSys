package com.example.laboras.serializers;

import com.example.laboras.ds.Course;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class CoursesGSONSerializer implements JsonSerializer<List<Course>> {
    @Override
    public JsonElement serialize(List<Course> courses, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Course.class, new CoursesGSONSerializer());
        Gson parser = gsonBuilder.create();

        for (Course l : courses) {
            jsonArray.add(parser.toJson(l));
        }
        return jsonArray;
    }
}
