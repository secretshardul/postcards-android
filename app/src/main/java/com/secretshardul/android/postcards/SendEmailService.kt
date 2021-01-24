package com.secretshardul.android.postcards

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://postcards-api-notifications.web.app/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

interface SendEmailInterface {
    @POST("/api/email/{email}")
    suspend fun sendEmail(@Path("email") email: String, @Query("key") key: String)
}

object SendEmailApi {
    val retrofitService: SendEmailInterface by lazy {
        retrofit.create(SendEmailInterface::class.java)
    }
}
