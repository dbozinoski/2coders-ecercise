package com.example.tmdbexercise.data.datasource.api.exception

import android.util.Log
import retrofit2.Response
import java.net.SocketTimeoutException

class ApiResponseHandler {

    fun <T> handleResponse(response: Response<T>, TAG: String): Result<T & Any>? {
        try {
            // Get the response code
            val statusCode = response.code()

            if (response.isSuccessful && response.body() != null) {
                when (statusCode) {
                    200 -> {
                        val body = response.body()
                        if (body != null) {
                            return Result.success(body)
                        } else {
                            return Result.failure(Exception("Success, but empty body"))
                        }
                    }

                    201 -> {
                        return Result.success(response.body()!!)
                    }
                }
                Log.d(TAG, "Success: Response code = $statusCode")
            } else {
                Log.e(TAG, "Error: Response code = $statusCode")
                return Result.failure(Exception("Error: Response code = $statusCode"))
            }
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "Timeout: ${e.message}")
            // Handle SocketTimeoutException
            return Result.failure(Exception("Network Timeout. Please try again."))
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            // Handle other exceptions
            return Result.failure(e)
        }
        return null
    }
}