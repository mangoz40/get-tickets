package com.example.gettickets.api

import retrofit2.http.GET
import retrofit2.Response

import com.example.gettickets.model.Event
import retrofit2.http.Path

interface EventApiService {
    @GET("events")
    suspend fun getEvents(): Response<List<Event>>

    @GET("events/{id}")
    suspend fun getEventById(@Path("id") eventId: Int): Response<Event>
}