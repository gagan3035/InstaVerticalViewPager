package com.example.gagan.lbbtask.services;

import com.example.gagan.lbbtask.model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Gagan on 5/28/2016.
 */

public interface GetTokenService {

//    @POST("/oauth/access_token")
//      Call<TokenResponse> getAccessToken(@Body TokenRequest tokenRequest); //so this returns a token response and we need to build a pojo for that.

    @FormUrlEncoded
    @POST("/oauth/access_token")
    Call<TokenResponse> getAccessToken(@Field("client_id") String client_id, @Field("client_secret") String client_secret,
                                       @Field("redirect_uri") String redirect_uri, @Field("grant_type") String grant_type,
                                       @Field("code") String code);
}
