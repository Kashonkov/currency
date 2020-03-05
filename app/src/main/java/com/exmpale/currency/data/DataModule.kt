package com.exmpale.currency.data

import android.content.Context
import com.exmpale.api.ResponseConverter
import com.exmpale.currency.api.CurrencyApi
import com.exmpale.currency.data.helper.ErrorHandlerImpl
import com.exmpale.currency.data.repository.RepositoryImpl
import com.exmpale.currency.domain.helper.ErrorHandler
import com.exmpale.currency.domain.repository.Repository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * @author Kashonkov Nikita
 */
@Module
class DataModule {
    @Provides
    @Singleton
    fun currencyApi(retrofit: Retrofit): CurrencyApi = retrofit.create(CurrencyApi::class.java)

    @Provides
    @Singleton
    fun repository(api: CurrencyApi, converter: ResponseConverter): Repository = RepositoryImpl(api, converter)

    @Provides
    @Singleton
    fun errorHandler(context: Context): ErrorHandler = ErrorHandlerImpl(context)
}