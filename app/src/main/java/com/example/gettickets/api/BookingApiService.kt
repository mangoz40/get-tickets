package com.example.gettickets.api

import com.example.gettickets.model.BookingRequest
import com.example.gettickets.model.BookingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BookingApiService {
    @POST("book-event")
    suspend fun createBooking(@Body booking: BookingRequest): Response<BookingResponse>
}