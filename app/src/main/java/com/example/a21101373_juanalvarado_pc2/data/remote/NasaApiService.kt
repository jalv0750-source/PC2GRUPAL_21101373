package com.example.a21101373_juanalvarado_pc2.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {

    @GET("planetary/apod")
    suspend fun getApodImages(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<ApodResponse>

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidFeed(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): NeoFeedResponse
}
