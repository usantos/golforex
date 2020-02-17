package br.com.golforex.util.services;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import br.com.golforex.util.interfaces.ApiInterface;
import br.com.golforex.util.interfaces.StatusInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForexApi {

    private ApiInterface apiInterface;
    private static ForexApi forexApi;

    public static ForexApi getInstance() {
        if (forexApi == null) {
            forexApi = new ForexApi();
        }
        return forexApi;
    }

    private ForexApi() {
        apiInterface = ClientService.create(ApiInterface.class);
    }


    /**
     * Get the latest forex and the base is the USD
     *
     * @param  statusInterface  StatusInterface
     * @return      success ir fail with the response
     */
    public void getLatest(final StatusInterface statusInterface) {
        apiInterface.getLatestForex().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    statusInterface.success(response.body());
                } else {
                    statusInterface.fail("Internal Problem " + response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable erThrowable) {
                statusInterface.fail("Server Problem " + erThrowable.getMessage());
            }
        });
    }

    /**
     * Get the forex by specific exchange
     *
     * @param  exchange  The value to get
     * @param  statusInterface  StatusInterface
     * @return      success ir fail with the response
     */
    public void getForexBySpecificExchange(String exchange, final StatusInterface statusInterface) {
        apiInterface.getForexBySpecificExchange(exchange).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    statusInterface.success(response.body());
                } else {
                    statusInterface.fail("Internal Problem " + response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable erThrowable) {
                statusInterface.fail("Server Problem " + erThrowable.getMessage());
            }
        });
    }


    /**
     * Get historical forex
     *
     * @param  lastDate  The value to get
     * @param  statusInterface  StatusInterface
     * @return      success ir fail with the response
     */
    public void getHistorical(String lastDate, final StatusInterface statusInterface) {
        apiInterface.getHistoricalForex(lastDate).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    statusInterface.success(response.body());
                } else {
                    statusInterface.fail("Internal Problem " + response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable erThrowable) {
                statusInterface.fail("Server Problem " + erThrowable.getMessage());
            }
        });
    }
}
