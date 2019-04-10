package com.example.cutetogether.neo4j;

import android.content.Context;
import android.util.Log;

import com.example.cutetogether.Network.GetDataService;
import com.example.cutetogether.Network.RetrofitClientInstance;
import com.example.cutetogether.Network.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class friendApi {
    private static final String TAG = "friendApi";
    public ArrayList<User> getFriendList(User user, Context context){
        ArrayList<User> res = new ArrayList<>();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<User> call = service.createUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "onResponse: neo response successful" + response.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Adding to Neo4j failed: " + t.toString());
            }
        });
        
        
        return res;
    }
}
