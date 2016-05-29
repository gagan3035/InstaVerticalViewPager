package com.example.gagan.lbbtask.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Gagan on 5/28/2016.
 */
public interface InstagramService {


    @GET("/v1/tags/{tag-name}/media/recent")
    Call<ResponseBody> getResponse(@Path("tag-name") String tagName, @Query("access_token") String accessToken,
                                   @Query("max_id") String maxId, @Query("min_id") String minId);
}