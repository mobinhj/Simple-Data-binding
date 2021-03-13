package com.example.sampledatabindingmvvm.Retrofit

import com.example.sampledatabindingmvvm.Json.JsonUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IUsersApi {
  @GET("users")
   fun getUser(
    @Query("username") userName: String,
    @Query("email") email : String)
  : Call<JsonUser>

}