package com.example.firebasechatapp.Notification;

import java.sql.Struct;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Retrofit retrofit=null;

    private static final String BASE_URL="https://fcm.googleapis.com/fcm/send";

    public static Retrofit getRetrofit(String s) {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)

                                        .addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;


    }


}
