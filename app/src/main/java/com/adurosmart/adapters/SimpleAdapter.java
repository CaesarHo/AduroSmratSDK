package com.adurosmart.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by best on 2016/7/8.
 */
public abstract class SimpleAdapter {

    /**
     * Attempts to read the wrapped object from its root object:
     * <pre>
     *
     *     {
     *         "key" : { wrapped object }
     *     }
     * </pre>
     */
    protected JsonObject readWrappedJsonObject(JsonElement root, String key) {
        if (root.isJsonObject()) {
            JsonObject wrapper = root.getAsJsonObject();
            return readJsonObject(wrapper, key);
        }
        return null;
    }

    protected JsonObject readJsonObject(JsonObject object, String key) {
        JsonElement element = object.get(key);
        if (element != null && element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        return null;
    }

    protected <T> T readObject(JsonObject object, String key, JsonDeserializationContext context, Type typeOfT) {
        JsonElement element = object.get(key);
        if (element != null) {
            return context.deserialize(element, typeOfT);
        }
        return null;
    }

    protected long readLong(JsonObject object, String key) {
        return readLong(object, key, 0);
    }

    protected long readLong(JsonObject object, String key, long defaultValue) {
        JsonElement element = object.get(key);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                return primitive.getAsLong();
            }
        }
        return defaultValue;
    }

    protected long[] readLongArray(JsonObject object, String key, JsonDeserializationContext context) {
        JsonElement element = object.get(key);
        if (element != null && element.isJsonArray()) {
            return context.deserialize(element, long[].class);
        }
        return new long[]{};
    }

    protected int readInt(JsonObject object, String key) {
        return readInt(object, key, 0);
    }

    protected int readInt(JsonObject object, String key, int defaultValue) {
        JsonElement element = object.get(key);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isNumber()) {
                return primitive.getAsInt();
            }
        }
        return defaultValue;
    }

    protected boolean readBoolean(JsonObject object, String key) {
        JsonElement element = object.get(key);
        if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsBoolean();
        }
        return false;
    }

    protected String readString(JsonObject object, String key) {
        return readString(object, key, null);
    }

    protected String readString(JsonObject object, String key, String defaultValue) {
        JsonElement element = object.get(key);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                return primitive.getAsString();
            }
        }
        return defaultValue;
    }

    protected <T> List<T> readList(JsonObject object, String key, JsonDeserializationContext context, Type typeOfT) {
        JsonElement element = object.get(key);
        if (element != null && element.isJsonArray()) {
            Object list = context.deserialize(element, typeOfT);
            return (List<T>) list;
        }
        return Collections.emptyList();
    }
}

