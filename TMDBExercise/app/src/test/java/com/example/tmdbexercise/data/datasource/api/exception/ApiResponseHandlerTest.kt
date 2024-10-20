package com.example.tmdbexercise.data.datasource.api.exception

import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.SocketTimeoutException

class ApiResponseHandlerTest {

    private lateinit var apiResponseHandler: ApiResponseHandler
    private val TAG = "ApiResponseHandlerTest"

    @Before
    fun setUp() {
        apiResponseHandler = ApiResponseHandler()
    }

    @Test
    fun `handleResponse should return success result when response is 200 and body is not null`() {
        val responseBody = "Success" // This can be any type of object
        val response = Response.success(200, responseBody)

        val result = apiResponseHandler.handleResponse(response, TAG)

        assertTrue(result?.isSuccess == true)
        assertEquals(responseBody, result?.getOrNull())
    }

    @Test
    fun `handleResponse should return failure result when response is 200 but body is null`() {
        val response = Response.success<String>(200, null)

        val result = apiResponseHandler.handleResponse(response, TAG)

        assertTrue(result?.isFailure == true)
        assertTrue(result?.exceptionOrNull() is Exception)
        assertEquals("Success, but empty body", result?.exceptionOrNull()?.message)
    }

    @Test
    fun `handleResponse should return success result when response is 201 and body is not null`() {
        val responseBody = "Created"
        val response = Response.success(201, responseBody)

        val result = apiResponseHandler.handleResponse(response, TAG)

        assertTrue(result?.isSuccess == true)
        assertEquals(responseBody, result?.getOrNull())
    }

    @Test
    fun `handleResponse should return failure result for non-200, non-201 response codes`() {
        val response = mockk<Response<String>>()
        every { response.isSuccessful } returns false
        every { response.code() } returns 404

        val result = apiResponseHandler.handleResponse(response, TAG)

        assertTrue(result?.isFailure == true)
        assertEquals("Error: Response code = 404", result?.exceptionOrNull()?.message)
    }

    @Test
    fun `handleResponse should return failure result when SocketTimeoutException occurs`() {
        val response = mockk<Response<String>>()
        every { response.code() } throws SocketTimeoutException("Timeout")

        val result = apiResponseHandler.handleResponse(response, TAG)

        assertTrue(result?.isFailure == true)
        assertEquals("Network Timeout. Please try again.", result?.exceptionOrNull()?.message)
    }

    @Test
    fun `handleResponse should return failure result when generic exception occurs`() {
        val response = mockk<Response<String>>()
        every { response.code() } throws RuntimeException("Unknown error")

        val result = apiResponseHandler.handleResponse(response, TAG)

        assertTrue(result?.isFailure == true)
        assertEquals("Unknown error", result?.exceptionOrNull()?.message)
    }
}