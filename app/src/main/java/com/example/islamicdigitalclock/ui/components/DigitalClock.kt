package com.example.islamicdigitalclock.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.util.Locale

@Composable
fun DigitalClock(time: LocalTime) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            String.format(Locale.ENGLISH, "%02d:%02d:%02d", time.hour, time.minute, time.second),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
