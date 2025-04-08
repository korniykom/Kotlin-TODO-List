package com.korniykom.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import javax.inject.Inject

class NetworkApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getPublicIp(): String {
        return client.get("https://api.ipify.org").bodyAsText()
    }
}