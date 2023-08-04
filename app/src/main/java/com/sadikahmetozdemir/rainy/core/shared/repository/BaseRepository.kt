package com.sadikahmetozdemir.rainy.core.shared.repository

abstract class BaseRepository {
    suspend fun <T> execute(request: suspend () -> T): T {
        return try {
            request.invoke()
        } catch (exception: Exception) {
            throw (exception)
        }
    }
}