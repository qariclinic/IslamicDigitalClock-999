package com.example.islamicdigitalclock

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat

class AdhanNotificationService : Service() {
    private var player: MediaPlayer? = null
    private var ringtone: Ringtone? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }
        val name = intent?.getStringExtra(AdhanScheduler.EXTRA_NAME) ?: "نماز"
        val notification = buildNotification(name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIF_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(NOTIF_ID, notification)
        }
        playAdhan()
        return START_NOT_STICKY
    }

    private fun playAdhan() {
        releaseAudio()
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        val mp = try {
            MediaPlayer.create(this, R.raw.adhan, attrs, AudioManager.AUDIO_SESSION_ID_GENERATE)
        } catch (_: Exception) {
            null
        }
        if (mp != null) {
            player = mp
            mp.setOnCompletionListener { stopSelf() }
            mp.start()
        } else {
            // adhan.mp3 خالی/خراب ہو تو فون کی ڈیفالٹ نوٹیفکیشن آواز
            ringtone = RingtoneManager.getRingtone(
                this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            )?.also { it.play() }
            handler.postDelayed({ stopSelf() }, 8000)
        }
    }

    private fun buildNotification(prayerName: String): Notification {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(CHANNEL_ID, "اذان نوٹیفکیشن", NotificationManager.IMPORTANCE_DEFAULT)
            ch.setSound(null, null) // آواز MediaPlayer چلاتا ہے
            nm.createNotificationChannel(ch)
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val open = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), flags)
        val stop = PendingIntent.getService(
            this, 1, Intent(this, AdhanNotificationService::class.java).setAction(ACTION_STOP), flags
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("اذان کا وقت")
            .setContentText("$prayerName کی اذان کا وقت ہو گیا ہے")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(open)
            .addAction(0, "بند کریں", stop)
            .setOngoing(true)
            .build()
    }

    private fun releaseAudio() {
        player?.release()
        player = null
        ringtone?.stop()
        ringtone = null
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        releaseAudio()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val CHANNEL_ID = "adhan_channel_v2"
        private const val NOTIF_ID = 1
        private const val ACTION_STOP = "com.example.islamicdigitalclock.STOP_ADHAN"
    }
}
