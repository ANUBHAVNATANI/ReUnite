package com.hacks.reunite.Repository;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.hacks.reunite.Contract.ApiContract.POST_IMAGE;

public interface RetrofitInterface {

    @FormUrlEncoded
    @POST(POST_IMAGE)
    Call<JsonElement> postImage(
            @Field("image")String pixelValues
    );
}
