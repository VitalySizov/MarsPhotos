package com.sizov.vitaly.marsphotos;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static Gson gson;
    private static NasaApi api;

    public static OkHttpClient getOkHttpClient() {

        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    public static Retrofit getRetrofit() {

        if (gson == null) {
            gson = new Gson();
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.SERVER_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static NasaApi getApi() {

        if (api == null) {
            api = getRetrofit().create(NasaApi.class);
        }
        return api;
    }
}
