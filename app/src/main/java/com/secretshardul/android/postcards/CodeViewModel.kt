package com.secretshardul.android.postcards

import androidx.lifecycle.ViewModel

class CodeViewModel : ViewModel() {
    private val repository = Repository()

    suspend fun sendEmail(email: String, key: String) = repository.sendEmail(email, key)
}
