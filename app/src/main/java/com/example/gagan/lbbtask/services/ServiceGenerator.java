package com.example.gagan.lbbtask.services;
import com.example.gagan.lbbtask.constants.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gagan on 5/28/2016.
 */
public class ServiceGenerator{

    public static GetTokenService createTokenService(){
        return getRetrofit().create(GetTokenService.class);
    }


    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

