package com.example.islamicdigitalclock

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.batoulapps.adhan.Coordinates

object LocationHelper {
    /** ڈیفالٹ: لاہور */
    private val DEFAULT = Coordinates(31.5204, 74.3587)

    fun current(context: Context): Coordinates {
        val granted =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        if (granted) {
            try {
                val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val best = lm.getProviders(true)
                    .mapNotNull { runCatching { lm.getLastKnownLocation(it) }.getOrNull() }
                    .maxByOrNull { it.time }
                if (best != null) {
                    Prefs.saveCoordinates(context, best.latitude, best.longitude)
                    return Coordinates(best.latitude, best.longitude)
                }
            } catch (_: SecurityException) {
            }
        }
        Prefs.savedCoordinates(context)?.let { return Coordinates(it.first, it.second) }
        return DEFAULT
    }
}
