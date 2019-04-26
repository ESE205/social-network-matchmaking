package com.example.cutetogether.Network;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetDataService {

    @POST("users/create")
    Call<User> createUser(@Body User user);

    @POST("friends/getfriends")
    Call<ArrayList<User>> getFriends(@Body User user);

    @POST("friends/getsuggestedfriends")
    Call<ArrayList<User>> getSuggestedFriends(@Body User user);

    @POST("friends/getrequests")
    Call<ArrayList<User>> getFriendRequest(@Body User user);

    @POST("friends/getsent")
    Call<ArrayList<User>> getFriendSent(@Body User user);

    @POST("friends/add")
    Call<String> addFriend(@Body ArrayList<User> user);

    @POST("friends/accept")
    Call<String> acceptFriend(@Body ArrayList<User> user);

    @POST("friends/deny")
    Call<String> denyFriend(@Body ArrayList<User> user);

    @POST("match/getlist")
    Call<ArrayList<MatchObject>> getMatchList(@Body User user);

    @POST("match/getqueue")
    Call<ArrayList<MatchObject>> getQueue(@Body User user);

    @POST("match/endorseMatchInfo")
    Call<String> sendMatchInfo(@Body endorseMatchObject data);

    @POST("match/denyMatchInfo")
    Call<String> denyMatchInfo(@Body endorseMatchObject data);

    @POST("match/acceptMatch")
    Call<String> acceptMatch(@Body endorseMatchObject data);

    @POST("match/completeAcceptMatch")
    Call<String> completeAcceptMatch(@Body endorseMatchObject data);

    @POST("match/denyMatch")
    Call<String> denyMatch(@Body endorseMatchObject data);


}
