package com.example.islamicdigitalclock

import android.content.Context

object Prefs {
    private fun sp(c: Context) = c.getSharedPreferences("idc_prefs", Context.MODE_PRIVATE)

    fun adhanEnabled(c: Context): Boolean = sp(c).getBoolean("adhan_enabled", true)
    fun setAdhanEnabled(c: Context, v: Boolean) { sp(c).edit().putBoolean("adhan_enabled", v).apply() }

    /** ہجری تاریخ میں دن کی کمی/زیادتی (چاند کی رویت کے مطابق) */
    fun hijriOffset(c: Context): Int = sp(c).getInt("hijri_offset", 0)
    fun setHijriOffset(c: Context, v: Int) { sp(c).edit().putInt("hijri_offset", v).apply() }

    fun savedCoordinates(c: Context): Pair<Double, Double>? {
        val s = sp(c)
        val la = s.getString("lat", null)?.toDoubleOrNull()
        val lo = s.getString("lon", null)?.toDoubleOrNull()
        return if (la != null && lo != null) la to lo else null
    }

    fun saveCoordinates(c: Context, lat: Double, lon: Double) {
        sp(c).edit().putString("lat", lat.toString()).putString("lon", lon.toString()).apply()
    }
}
