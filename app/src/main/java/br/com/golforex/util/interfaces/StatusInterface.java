package br.com.golforex.util.interfaces;

import com.google.gson.JsonObject;

import org.json.JSONException;

public interface StatusInterface {
    void success(JsonObject jsonObject);

    void fail(String message);
}
