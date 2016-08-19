package com.jpardogo.android.listbuddies.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by creditcloud on 8/16/16.
 */


public interface RetrofitAPI {
    public static final String BASE_URL = "http://7xvrxf.com1.z0.glb.clouddn.com/";

    @GET("config/images.json")
    Call<ImagesResponse> images();
}

