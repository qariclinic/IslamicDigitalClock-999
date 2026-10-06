package com.example.islamicdigitalclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

/** الارم کے وقت اذان سروس شروع کرتا ہے اور اگلی اذان شیڈیول کرتا ہے۔ */
class AdhanReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra(AdhanScheduler.EXTRA_NAME) ?: "نماز"
        if (Prefs.adhanEnabled(context)) {
            try {
                ContextCompat.startForegroundService(
                    context,
                    Intent(context, AdhanNotificationService::class.java)
                        .putExtra(AdhanScheduler.EXTRA_NAME, name)
                )
            } catch (_: Exception) {
            }
        }
        AdhanScheduler.scheduleAll(context)
    }
}
