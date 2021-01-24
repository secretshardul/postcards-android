package com.secretshardul.android.postcards

import androidx.annotation.Keep

@Keep
data class SendEmailRequestModel(
    val key: String,
    val email: String
)
