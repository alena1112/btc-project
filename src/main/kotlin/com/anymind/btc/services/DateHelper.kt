package com.anymind.btc.services

import java.util.*

class DateHelper {
    companion object {
        fun clearDate(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.time
        }

        fun addOneDay(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar[Calendar.HOUR_OF_DAY] += 1
            return calendar.time
        }
    }
}