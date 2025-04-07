package com.korniykom.domain.usecase

import com.korniykom.domain.repository.NetworkRepository
import javax.inject.Inject

class GetPublicIpUseCase @Inject constructor(
    private val networkRepository : NetworkRepository
) {
    suspend operator fun invoke() : String {
        return networkRepository.getPublicIp()
    }
}