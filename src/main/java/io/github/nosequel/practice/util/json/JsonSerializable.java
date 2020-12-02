package io.github.nosequel.practice.util.json;

import com.google.gson.JsonObject;

public abstract class JsonSerializable {

    public JsonSerializable(JsonObject object) {
    }

    public abstract JsonObject toJson();

}
