package com.exmpale.currency.data.repository

import com.exmpale.api.CurrencyApi
import com.exmpale.api.HttpClientBuilder
import com.exmpale.api.RetrofitBuilder
import com.exmpale.currency.data.model.convertToCurrencyEntity
import com.exmpale.currency.domain.entity.CurrencyEntity
import com.exmpale.currency.domain.repository.Repository
import io.reactivex.Single
import okhttp3.OkHttpClient

/**
 * @author Kashonkov Nikita
 */
class RepositoryImpl : Repository {
    override fun getCurrency(): Single<CurrencyEntity> {
        val okCLient = OkHttpClient()
        val client = HttpClientBuilder(okCLient)
        val retrofit = RetrofitBuilder(client).build()
        val currencyApi = retrofit.create(CurrencyApi::class.java)

        return currencyApi.getCurrency().map { convertToCurrencyEntity(it) }
    }

}