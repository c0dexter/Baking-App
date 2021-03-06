package pl.michaldobrowolski.bakingapp.api.service;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String TAG = ApiClient.class.getClass().getSimpleName();

    private static final String BASE_API_URL = "http://go.udacity.com/";
    public static Retrofit getClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d(TAG, "Retrofit client has been built successfully.");
        return retrofit;
    }
}
