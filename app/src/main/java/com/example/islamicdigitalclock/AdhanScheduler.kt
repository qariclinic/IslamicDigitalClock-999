package com.example.islamicdigitalclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.LocalDate

object AdhanScheduler {
    const val EXTRA_NAME = "prayer_name"

    private fun pendingIntent(context: Context, index: Int, name: String?): PendingIntent {
        val intent = Intent(context, AdhanReceiver::class.java)
        if (name != null) intent.putExtra(EXTRA_NAME, name)
        return PendingIntent.getBroadcast(
            context, index, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    /** ہر نماز کی اگلی اذان کا الارم لگاتا ہے۔ اذان کے بعد receiver خود دوبارہ شیڈیول کرتا ہے۔ */
    fun scheduleAll(context: Context) {
        val app = context.applicationContext
        if (!Prefs.adhanEnabled(app)) {
            cancelAll(app)
            return
        }
        val coords = LocationHelper.current(app)
        val nowMs = System.currentTimeMillis()
        val today = LocalDate.now()
        val list = PrayerTimesCalculator.forDate(coords, today) +
            PrayerTimesCalculator.forDate(coords, today.plusDays(1))
        val am = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val canExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || am.canScheduleExactAlarms()

        PrayerTimesCalculator.ADHAN_KEYS.forEachIndexed { i, key ->
            val p = list.firstOrNull { it.key == key && it.time.time > nowMs + 5000 }
                ?: return@forEachIndexed
            val pi = pendingIntent(app, i, p.name)
            try {
                if (canExact) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, p.time.time, pi)
                } else {
                    am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, p.time.time, pi)
                }
            } catch (_: SecurityException) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, p.time.time, pi)
            }
        }
    }

    fun cancelAll(context: Context) {
        val app = context.applicationContext
        val am = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        PrayerTimesCalculator.ADHAN_KEYS.indices.forEach { am.cancel(pendingIntent(app, it, null)) }
    }
}
