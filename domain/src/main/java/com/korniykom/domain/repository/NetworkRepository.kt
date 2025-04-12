package com.korniykom.domain.repository

interface NetworkRepository {
    suspend fun getPublicIp() : String
}