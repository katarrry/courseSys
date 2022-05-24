package com.example.laboras.serializers;

import com.google.gson.*;
import com.example.laboras.ds.User;

import java.lang.reflect.Type;

public class UserGSONSerializer implements JsonSerializer<User>{
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject userJson = new JsonObject();
        userJson.addProperty("userId", user.getId());
        userJson.addProperty("userLogin", user.getLogin());
        userJson.addProperty("userPsw", user.getPsw());
        userJson.addProperty("userName", user.getName());
        userJson.addProperty("userSurname", user.getSurname());
        userJson.addProperty("userEmail", user.getEmail());
        return userJson;
    }
}
