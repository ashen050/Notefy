package com.example.notesync.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ClockApiService {

    @GET("timezone/{timezone}")
    Call<ClockResponse> getCurrentTime(@Path("timezone") String timezone);
}
