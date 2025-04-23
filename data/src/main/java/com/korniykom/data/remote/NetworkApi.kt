package com.korniykom.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import javax.inject.Inject

class NetworkApi @Inject constructor(
    private val client : HttpClient
) {
    suspend fun getPublicIp() : String {
        val response = client.get("https://api.ipify.org")
        if (! response.status.isSuccess()) {
            throw RuntimeException("Failed to get IP: ${response.status}")
        }
        return response.bodyAsText()
    }
}