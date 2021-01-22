package com.secretshardul.android.postcards

import androidx.annotation.Keep
import java.util.Date

@Keep
data class PostcardModel(
    val title: String = "",
    val body: String = "",
    val imageUrl: String = "",
    val time: Date = Date(0)
)
