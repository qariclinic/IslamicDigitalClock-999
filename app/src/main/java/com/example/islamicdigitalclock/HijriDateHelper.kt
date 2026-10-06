package com.example.islamicdigitalclock

import java.time.LocalDate
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

data class HijriDate(val year: Int, val month: Int, val day: Int)

object HijriDateHelper {
    private val hijriMonths = listOf(
        "محرم الحرام", "صفر المظفر", "ربیع الاول", "ربیع الثانی",
        "جمادی الاول", "جمادی الثانی", "رجب المرجب", "شعبان المعظم",
        "رمضان المبارک", "شوال المکرم", "ذوالقعدہ", "ذوالحجہ"
    )
    private val gregorianMonths = listOf(
        "جنوری", "فروری", "مارچ", "اپریل", "مئی", "جون",
        "جولائی", "اگست", "ستمبر", "اکتوبر", "نومبر", "دسمبر"
    )

    /** اُمّ القریٰ تقویم (java.time) + رویت کے مطابق دن کا ایڈجسٹمنٹ */
    fun hijriOf(date: LocalDate, offsetDays: Int = 0): HijriDate {
        val h = HijrahDate.from(date.plusDays(offsetDays.toLong()))
        return HijriDate(
            h.get(ChronoField.YEAR),
            h.get(ChronoField.MONTH_OF_YEAR),
            h.get(ChronoField.DAY_OF_MONTH)
        )
    }

    fun formatHijri(h: HijriDate): String = "${h.day} ${hijriMonths[h.month - 1]} ${h.year} ھ"

    fun formatGregorian(d: LocalDate): String =
        "${d.dayOfMonth} ${gregorianMonths[d.monthValue - 1]} ${d.year} ء"
}
