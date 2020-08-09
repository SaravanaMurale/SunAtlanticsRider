package com.example.sunatlanticsrider.retrofit;


import com.example.sunatlanticsrider.utils.BaseURL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static Retrofit retrofit = null;
    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    public static Retrofit getAPIClient() {

        if (retrofit == null) {

            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();


            retrofit = new Retrofit.Builder().baseUrl(BaseURL.DOMAIN_NAME)

                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }

        return retrofit;
    }

    /*public static ApiInterface getApiInterface() {

        ApiInterface api = ApiClient.getAPIClient().create(ApiInterface.class);

        return api;

    }*/



}
