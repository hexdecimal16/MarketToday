package com.dhairytripathi.markettoday.data.model

import android.os.Parcelable
import com.dhairytripathi.markettoday.data.model.History
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Stock(val name: String, val symbol: String, val id: Int, val data: @RawValue MutableList<History>, val currentPrice: Float, val currentChange: Float) : Parcelable {}
