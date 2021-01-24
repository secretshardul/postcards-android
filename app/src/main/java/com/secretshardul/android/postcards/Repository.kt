package com.secretshardul.android.postcards


class Repository {
    suspend fun sendEmail(email: String, key: String) = SendEmailApi
        .retrofitService
        .sendEmail(email, key)
}
