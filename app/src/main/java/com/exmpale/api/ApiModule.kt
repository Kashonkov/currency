package com.exmpale.api

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * @author Kashonkov Nikita
 */
@Module
class ApiModule {
    @Provides
    @Singleton
    fun httpClient(): OkHttpClient = OkHttpClient()

    @Provides
    @Singleton
    fun apiRetrofit(retrofitBuilder: RetrofitBuilder): Retrofit = retrofitBuilder.build()

}