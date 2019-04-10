package com.example.cutetogether.Network;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetDataService {

    @POST("users/create")
    Call<User> createUser(@Body User user);

    @POST("friends/getrequests")
    Call<ArrayList<FriendRequest>> getFriendRequest(@Body User user);

    @POST("friends/getsent")
    Call<ArrayList<FriendRequest>> getFriendSent(@Body User user);

    @POST("friends/add")
    Call<FriendRequest> addFriend(@Body User user, User user2);

    @POST("friends/accept")
    Call<User> acceptFriend(@Body User user, User user2);

    @POST("match/getlist")
    Call<ArrayList<MatchObject>> getMatchList(@Body User user);

    @POST("match/endorseMatchInfo")
    Call<MatchObject> sendMatchInfo(@Body MatchObject matchObject, User user);

    @POST("match/denyMatchInfo")
    Call<MatchObject> denyMatchInfo(@Body MatchObject matchObject, User user);

    @POST("match/acceptMatch")
    Call<MatchObject> acceptMatch(@Body MatchObject matchObject, User user);

    @POST("match/denyMatch")
    Call<MatchObject> denyMatch(@Body MatchObject matchObject, User user);


}
