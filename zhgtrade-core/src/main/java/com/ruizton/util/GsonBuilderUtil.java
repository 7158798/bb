package com.ruizton.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by sunpeng on 2016/9/13.
 */
public class GsonBuilderUtil {
    public static Gson create() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(java.util.Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                json.getAsJsonPrimitive().getAsLong();
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = gb.create();
        return gson;
    }
}
