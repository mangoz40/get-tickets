package com.example.gettickets.network

//import com.example.gettickets.model.Booking
import com.example.gettickets.model.Event
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("events")
    suspend fun getEvents(): List<Event>

    @GET("events/{event}")
    suspend fun getEvent(@Path("event") eventId: Int): Event

    /*@FormUrlEncoded
    @POST("bookings")
    suspend fun bookTicket(
        @Field("event_id") eventId: Int,
        @Field("user_id") userId: String,
        @Field("ticket_quantity") ticketQuantity: Int
    ): Response<Booking>

    @GET("bookings/{booking}")
    suspend fun getBooking(@Path("booking") bookingId: Int): Booking*/
}