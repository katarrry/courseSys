package com.example.laboras.serializers;

import com.example.laboras.ds.Course;
import com.example.laboras.ds.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class CourseGSONSerializer implements JsonSerializer<Course> {
    @Override
    public JsonElement serialize(Course course, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject courseJson = new JsonObject();
        courseJson.addProperty("courseId", course.getId());
        courseJson.addProperty("courseTitle", course.getTitle());

        return courseJson;
    }
}
