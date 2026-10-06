package com.example.islamicdigitalclock

data class IslamicEvent(val name: String, val description: String)

object IslamicEventsHelper {
    private val events = mapOf(
        "1/1" to IslamicEvent("اسلامی نیا سال", "ہجری سال کا آغاز"),
        "1/10" to IslamicEvent("عاشورہ", "حضرت امام حسینؓ کی شہادت"),
        "3/12" to IslamicEvent("عید میلاد النبیؐ", "نبی کریمؐ کی ولادت"),
        "9/1" to IslamicEvent("رمضان المبارک", "روزوں کا مہینہ شروع"),
        "9/27" to IslamicEvent("لیلۃ القدر", "قرآن نازل ہونے کی رات"),
        "10/1" to IslamicEvent("عید الفطر", "رمضان کے بعد خوشی کا دن"),
        "12/9" to IslamicEvent("یوم عرفہ", "حج کا اہم دن"),
        "12/10" to IslamicEvent("عید الاضحی", "قربانی کا دن")
    )

    fun getEvents(h: HijriDate): List<IslamicEvent> =
        events["${h.month}/${h.day}"]?.let { listOf(it) } ?: emptyList()
}
