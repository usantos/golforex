package br.com.golforex.util.interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/latest?base=USD")
    Call<JsonObject> getLatestForex();

    @GET("/latest")
    Call<JsonObject> getForexBySpecificExchange(@Query("base") String exchange);

    @GET("/{lastDate}?base=USD")
    Call<JsonObject> getHistoricalForex(@Path("lastDate") String lastDate);
}
