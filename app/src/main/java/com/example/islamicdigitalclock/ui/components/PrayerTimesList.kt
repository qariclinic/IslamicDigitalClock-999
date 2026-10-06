package com.example.islamicdigitalclock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.islamicdigitalclock.PrayerTime
import com.example.islamicdigitalclock.toClock

@Composable
fun PrayerTimesList(prayerTimes: List<PrayerTime>, nextKey: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "نماز کے اوقات", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            prayerTimes.forEach { p ->
                val isNext = p.key == nextKey
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .background(
                            if (isNext) MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                            else androidx.compose.ui.graphics.Color.Transparent,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(p.name, fontSize = 16.sp, fontWeight = if (isNext) FontWeight.Bold else FontWeight.Normal)
                    Text(p.time.toClock(), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
