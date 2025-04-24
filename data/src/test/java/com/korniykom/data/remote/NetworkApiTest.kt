package com.korniykom.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class NetworkApiTest {
    @Test
    fun getPublicIp_returns_ip_address_response_text_plain() : Unit = runTest {
        val expectedIp = "1.1.1.1"
        val mockEngine = MockEngine { request ->
            assertEquals("https://api.ipify.org" , request.url.toString())
            assertEquals(request.method , HttpMethod.Get)

            respond(
                content = ByteReadChannel(expectedIp) ,
                status = HttpStatusCode.OK ,
                headers = headersOf(HttpHeaders.ContentType , "text/plain")
            )
        }

        val httpClient = HttpClient(mockEngine)
        val networkApi = NetworkApiImpl(httpClient)

        val result = networkApi.getPublicIp()

        assertEquals(result , expectedIp)
    }

    @Test
    fun getPublicIp_returns_ip_address_response_application_text() : Unit = runTest {
        val expectedIp = "2.2.2.2"
        val mockEngine = MockEngine { request ->
            assertEquals("https://api.ipify.org" , request.url.toString())
            assertEquals(request.method , HttpMethod.Get)

            respond(
                content = ByteReadChannel(expectedIp) ,
                status = HttpStatusCode.OK ,
                headers = headersOf(HttpHeaders.ContentType , "application/text")
            )
        }

        val httpClient = HttpClient(mockEngine)
        val networkApi = NetworkApiImpl(httpClient)

        val result = networkApi.getPublicIp()

        assertEquals(result , expectedIp)
    }

    @Test
    fun getPublicIp_throws_exception_when_status_is_500() : Unit = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("IP address is unavailable") ,
                status = HttpStatusCode.ServiceUnavailable ,
                headers = headersOf(HttpHeaders.ContentType , "text/plain")
            )
        }

        val httpClient = HttpClient(mockEngine)
        val networkApi = NetworkApiImpl(httpClient)

        try {
            networkApi.getPublicIp()
            Assert.fail("getPublicIp didn't not throw an exception")
        } catch (e : Exception) {

        }
    }

    @Test
    fun getPublicIp_throws_exception_when_status_is_400() : Unit = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("Bad Request") ,
                status = HttpStatusCode.BadRequest ,
                headers = headersOf(HttpHeaders.ContentType , "text/plain")
            )
        }

        val httpClient = HttpClient(mockEngine)
        val networkApi = NetworkApiImpl(httpClient)

        try {
            networkApi.getPublicIp()
            Assert.fail("getPublicIp didn't not throw an exception")
        } catch (e : Exception) {

        }
    }

}