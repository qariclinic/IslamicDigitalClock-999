package com.example.islamicdigitalclock

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.islamicdigitalclock.ui.components.DigitalClock
import com.example.islamicdigitalclock.ui.components.EventHighlight
import com.example.islamicdigitalclock.ui.components.PrayerTimesList
import com.example.islamicdigitalclock.ui.theme.IslamicDigitalClockTheme
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IslamicDigitalClockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

private fun missingPermissions(context: Context): Array<String> {
    val wanted = mutableListOf(Manifest.permission.ACCESS_COARSE_LOCATION)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        wanted += Manifest.permission.POST_NOTIFICATIONS
    }
    return wanted.filter {
        ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
    }.toTypedArray()
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var adhanEnabled by remember { mutableStateOf(Prefs.adhanEnabled(context)) }
    var offset by remember { mutableIntStateOf(Prefs.hijriOffset(context)) }
    var coords by remember { mutableStateOf(LocationHelper.current(context)) }
    var now by remember { mutableStateOf(LocalDateTime.now()) }

    val permLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        coords = LocationHelper.current(context)
        AdhanScheduler.scheduleAll(context)
    }

    LaunchedEffect(Unit) {
        val missing = missingPermissions(context)
        if (missing.isNotEmpty()) permLauncher.launch(missing)
        AdhanScheduler.scheduleAll(context)
    }
    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            delay(1000)
        }
    }

    val today = now.toLocalDate()
    val hijri = remember(today, offset) { HijriDateHelper.hijriOf(today, offset) }
    val events = remember(hijri) { IslamicEventsHelper.getEvents(hijri) }
    val prayerTimes = remember(coords, today) { PrayerTimesCalculator.forDate(coords, today) }
    val tomorrowTimes = remember(coords, today) { PrayerTimesCalculator.forDate(coords, today.plusDays(1)) }
    val nowMs = System.currentTimeMillis()
    val next = (prayerTimes + tomorrowTimes).firstOrNull { it.time.time > nowMs }
    val nextKey = next?.key

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.credit),
                fontSize = 14.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            DigitalClock(now.toLocalTime())

            if (next != null) {
                val secs = ((next.time.time - nowMs) / 1000).coerceAtLeast(0)
                Text(
                    "اگلی نماز: ${next.name} — " + String.format(
                        Locale.ENGLISH, "%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60
                    ),
                    fontSize = 16.sp, color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "ہجری تاریخ: ${HijriDateHelper.formatHijri(hijri)}",
                        fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "عیسوی تاریخ: ${HijriDateHelper.formatGregorian(today)}",
                        fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("رویت کے مطابق ایڈجسٹمنٹ: ${if (offset > 0) "+" else ""}$offset", fontSize = 14.sp)
                        Row {
                            TextButton(onClick = {
                                if (offset > -2) { offset--; Prefs.setHijriOffset(context, offset) }
                            }) { Text("−", fontSize = 20.sp) }
                            TextButton(onClick = {
                                if (offset < 2) { offset++; Prefs.setHijriOffset(context, offset) }
                            }) { Text("+", fontSize = 20.sp) }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            if (events.isNotEmpty()) {
                EventHighlight(events = events)
                Spacer(Modifier.height(16.dp))
            }

            PrayerTimesList(prayerTimes = prayerTimes, nextKey = nextKey)
            Spacer(Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("اذان نوٹیفکیشن", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Switch(checked = adhanEnabled, onCheckedChange = {
                        adhanEnabled = it
                        Prefs.setAdhanEnabled(context, it)
                        AdhanScheduler.scheduleAll(context)
                    })
                }
            }
        }
    }
}
