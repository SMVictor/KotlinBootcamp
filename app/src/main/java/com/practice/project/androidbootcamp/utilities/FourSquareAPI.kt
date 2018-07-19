package com.practice.project.androidbootcamp.utilities

import com.practice.project.androidbootcamp.model.JsonResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FourSquareAPI {

    @GET("venues/search")
    fun requestSearch(
            @Query("client_id") client_id: String,
            @Query("client_secret") client_secret: String,
            @Query("v") v: String,
            @Query("ll") ll: String,
            @Query("radius") radius: String): Call<JsonResponse>
}