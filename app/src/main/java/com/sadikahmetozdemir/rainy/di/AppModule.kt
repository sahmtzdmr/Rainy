package com.sadikahmetozdemir.rainy.di

import android.content.Context
import com.sadikahmetozdemir.rainy.core.service.WeatherAPI
import com.sadikahmetozdemir.rainy.core.shared.repository.DefaultRepository
import com.sadikahmetozdemir.rainy.utils.Constants
import com.sadikahmetozdemir.rainy.utils.DataHelperManager
import com.sadikahmetozdemir.rainy.utils.NetworkInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(retrofitClient: Retrofit) =
        retrofitClient.create(WeatherAPI::class.java)

    @Provides
    fun provideDataManager(@ApplicationContext context: Context): DataHelperManager {
        return DataHelperManager(context)
    }

    @Provides
    @Singleton
    fun provideInterceptor(
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideDefaultRepository(
        weatherAPI: WeatherAPI
    ): DefaultRepository {
        return DefaultRepository(weatherAPI)
    }
}

