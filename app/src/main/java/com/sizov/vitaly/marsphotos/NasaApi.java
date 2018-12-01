package com.sizov.vitaly.marsphotos;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NasaApi {

    @GET("latest_photos" + BuildConfig.API_KEY)
    Call<Photo> getLatestPhotos();
}
