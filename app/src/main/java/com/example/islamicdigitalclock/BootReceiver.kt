package com.example.islamicdigitalclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/** ریبوٹ، ایپ اپڈیٹ، وقت یا ٹائم زون بدلنے پر اذان کے الارم دوبارہ لگاتا ہے۔ */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        AdhanScheduler.scheduleAll(context)
    }
}
