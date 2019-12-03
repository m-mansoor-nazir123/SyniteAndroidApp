package com.example.firebasechatapp.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAxYaW2U4:APA91bFr-xlESaUJjzkvLNjapM_UvqvPHPGHyTmqMGtu5C1kddfEit8_LsCOo_d5l_BBEwnHSJLwnIjvRq0C7A6v1OsKTY_ZRYmASRQUmqMaYjti0eS4a0tuC1dmsxJYAVnaJHPcBKY4"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
