package com.example.gettickets.api

import retrofit2.http.GET
import retrofit2.Response

import com.example.gettickets.model.Event

interface EventApiService {
    @GET("events")
    suspend fun getEvents(): Response<List<Event>>
}