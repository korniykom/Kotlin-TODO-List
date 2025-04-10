package com.korniykom.data.repository

import com.korniykom.data.remote.NetworkApi
import com.korniykom.domain.repository.NetworkRepository
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val networkApi : NetworkApi
) : NetworkRepository {
    override suspend fun getPublicIp() : String {
        return try {
            networkApi.getPublicIp()
        } catch (e : Exception) {
            "Problem with fetching public api"
        }
    }
}