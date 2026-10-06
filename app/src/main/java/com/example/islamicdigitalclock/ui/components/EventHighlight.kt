package com.example.islamicdigitalclock.ui.components
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.islamicdigitalclock.IslamicEvent
@Composable
fun EventHighlight(events: List<IslamicEvent>) {
    events.forEach { e ->
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFD700))
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(e.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(e.description, fontSize = 14.sp, color = Color.Black)
            }
        }
    }
}
