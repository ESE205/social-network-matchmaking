package com.example.cutetogether.Network;

import android.content.res.Resources;

import com.example.cutetogether.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit sRetrofit;
    private static String BASE_URL= Resources.getSystem().getString(R.string.url);

    public static Retrofit getRetrofitInstance(){

        if(sRetrofit == null){
           sRetrofit = new retrofit2.Retrofit.Builder()
                   .baseUrl(BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();
        }

        return sRetrofit;
    }

}
