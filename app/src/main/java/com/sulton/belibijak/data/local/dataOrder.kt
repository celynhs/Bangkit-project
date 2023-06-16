package com.sulton.belibijak.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class dataOrder(
    val nama : String,
    val rp : String,
    val pcs : String,
    val img : String
):Parcelable
