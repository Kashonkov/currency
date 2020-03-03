package com.exmpale.currency.data.repository

import com.exmpale.api.ResponseConverter
import com.exmpale.currency.api.CurrencyApi
import com.exmpale.currency.data.model.convertToCurrencyEntity
import com.exmpale.currency.domain.entity.CurrencyEntity
import com.exmpale.currency.domain.repository.Repository
import io.reactivex.Single

/**
 * @author Kashonkov Nikita
 */
class RepositoryImpl constructor(val currencyApi: CurrencyApi, val responseConverter: ResponseConverter) : Repository {
    override fun getCurrency(): Single<CurrencyEntity> {
        return currencyApi.getCurrency().map { convertToCurrencyEntity(it) }
    }
}