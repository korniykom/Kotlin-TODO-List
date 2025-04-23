package com.korniykom.data.repository

import com.korniykom.data.remote.NetworkApi
import com.korniykom.data.remote.NetworkApiImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NetworkRepositoryImplTest {
    @MockK(relaxUnitFun = true)
    private lateinit var mockNetworkApi: NetworkApi
    private lateinit var networkRepository: NetworkRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        networkRepository = NetworkRepositoryImpl(mockNetworkApi)
    }

    @Test
    fun getPublicIp_return_IP_address_from_NetworkApi(): Unit = runTest {
        val expectedIp = "1.1.1.1"
        coEvery { mockNetworkApi.getPublicIp() } returns expectedIp

        val result = networkRepository.getPublicIp()

        assertEquals(expectedIp, result)
        coVerify { mockNetworkApi.getPublicIp() }
    }

    @Test
    fun getPublicIp_throws_exception(): Unit = runTest {
        coEvery { mockNetworkApi.getPublicIp() } throws RuntimeException("Network Error")

        try {
            networkRepository.getPublicIp()
            Assert.fail("getPublicIp should thrown an error")
        } catch (e: Exception) {

        }

        coVerify { mockNetworkApi.getPublicIp() }
    }
}