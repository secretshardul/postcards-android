package com.secretshardul.android.postcards

import androidx.annotation.Keep

@Keep
data class UserDataModel(
    val fcmToken: String = "",
    val postcards: List<PostcardModel> = listOf()
)
