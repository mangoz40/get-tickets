package com.example.gettickets.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gettickets.api.EventApiService
import com.example.gettickets.model.Event
import com.example.gettickets.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val api: EventApiService
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getEvents(): Flow<Resource<List<Event>>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.getEvents()
            if (response.isSuccessful) {
                val ress = response.body()
                val events = response.body()?.map { it.toEvent() } ?: emptyList()
                emit(Resource.Success(events))
            } else {
                emit(Resource.Error("Failed to fetch events"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An error occurred"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun Event.toEvent(): Event {
        return Event(
            id = id,
            title = title,
            description = description,
            location = location,
            available_tickets = available_tickets!!.toInt(),
            price = price!!.toDouble(),
            date = date,
            created_at = created_at,
            updated_at = updated_at
        )
    }
}