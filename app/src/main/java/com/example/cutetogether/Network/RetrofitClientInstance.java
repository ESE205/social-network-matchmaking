package com.example.cutetogether.Network;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.cutetogether.R;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//partial https://stackoverflow.com/questions/32514410/logging-with-retrofit-2
public class RetrofitClientInstance {
    private static final String TAG = "RetrofitClientInstance";
    private static Retrofit sRetrofit;


    public static Retrofit getRetrofitInstance(Context context){

        Log.d(TAG, "getRetrofitInstance: HTTPClient");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if(sRetrofit == null){
            sRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(context.getString(R.string.url))
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return sRetrofit;
    }

}
