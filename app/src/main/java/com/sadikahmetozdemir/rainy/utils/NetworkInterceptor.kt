package com.sadikahmetozdemir.rainy.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class NetworkInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url: HttpUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("appid", Constants.API_KEY)
            .build()
        val request: Request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }
}