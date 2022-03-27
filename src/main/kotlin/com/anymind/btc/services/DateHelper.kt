package com.anymind.btc.services

import java.util.*

/**
 * Class Helper provides work with date
 */
class DateHelper {
    companion object {

        /**
         * Methods clears minutes, seconds and milliseconds from specified date
         */
        fun clearDate(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.time
        }

        /**
         * Methods adds one day in specified date
         */
        fun addOneDay(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar[Calendar.HOUR_OF_DAY] += 1
            return calendar.time
        }
    }
}