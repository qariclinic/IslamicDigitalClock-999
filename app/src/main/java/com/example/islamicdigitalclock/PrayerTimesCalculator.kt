package com.example.islamicdigitalclock

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.Madhab
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class PrayerTime(val key: String, val name: String, val time: Date)

private val clockFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

fun Date.toClock(): String =
    Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).format(clockFormat)

object PrayerTimesCalculator {
    /** اذان والی پانچ نمازیں */
    val ADHAN_KEYS = listOf("fajr", "dhuhr", "asr", "maghrib", "isha")

    private fun params() = CalculationMethod.KARACHI.parameters.also { it.madhab = Madhab.HANAFI }

    fun forDate(coordinates: Coordinates, date: LocalDate): List<PrayerTime> {
        val dc = DateComponents(date.year, date.monthValue, date.dayOfMonth)
        val pt = PrayerTimes(coordinates, dc, params())
        return listOf(
            PrayerTime("fajr", "فجر", pt.fajr),
            PrayerTime("sunrise", "طلوع آفتاب", pt.sunrise),
            PrayerTime("dhuhr", "ظہر", pt.dhuhr),
            PrayerTime("asr", "عصر", pt.asr),
            PrayerTime("maghrib", "مغرب", pt.maghrib),
            PrayerTime("isha", "عشاء", pt.isha)
        )
    }
}
