package com.sizov.vitaly.marsphotos.api;

import com.sizov.vitaly.marsphotos.BuildConfig;
import com.sizov.vitaly.marsphotos.Photo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NasaApi {

    @GET("latest_photos" + BuildConfig.API_KEY)
    Call<Photo> getLatestPhotos();
}
