package com.dhairytripathi.markettoday.data.model

import android.os.Parcelable
import java.text.DateFormat


data class History(val high: Float, val low: Float, val open: Float, val currentPrice: Float, val change: Float, val priceChange: Float, val timestamp: Long) {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(timestamp)
}
