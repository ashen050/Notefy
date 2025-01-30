package com.example.notesync.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//code attribution

//World Time Api (no date) Back to the overview about the World Time API. Available at: https://worldtimeapi.org/ (Accessed: 04 November 2024).

//(No date) YouTube. Available at: https://m.youtube.com/watch?v=_dCRQ9wta-I&pp=ygUoSG93IHRvIGZvIGEgYmlvZW50cmljcyBvbiBhbmRyaW9kIHN0dWlkbw%3D%3D (Accessed: 04 November 2024).
public class RetrofitInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://worldtimeapi.org/api/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
