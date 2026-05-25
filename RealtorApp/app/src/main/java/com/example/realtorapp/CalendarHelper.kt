package com.example.realtorapp

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import java.util.*

object CalendarHelper {

    fun addViewingToCalendar(context: Context, apartment: ApartmentInfo) {
        val startTime = Calendar.getInstance().apply {
            add(Calendar.HOUR, 2)
        }.timeInMillis

        val endTime = startTime + 60 * 60 * 1000

        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, "Просмотр квартиры №${apartment.apartmentNumber}")
            putExtra(CalendarContract.Events.DESCRIPTION,
                "Площадь: ${apartment.area} м² | Комнат: ${apartment.numRooms} | Санузел: ${apartment.bathroomType}")
            putExtra(CalendarContract.Events.EVENT_LOCATION, apartment.houseAddress)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
        }

        try {
            context.startActivity(intent)
            Toast.makeText(context, "Откройте календарь и сохраните событие", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Не удалось открыть календарь", Toast.LENGTH_SHORT).show()
        }
    }
}