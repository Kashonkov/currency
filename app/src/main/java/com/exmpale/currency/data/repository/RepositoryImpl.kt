package com.exmpale.currency.data.repository

import com.exmpale.api.ResponseConverter
import com.exmpale.currency.api.CurrencyApi
import com.exmpale.currency.data.model.convertToRatesEntity
import com.exmpale.currency.domain.entity.RatesEntity
import com.exmpale.currency.domain.repository.Repository
import io.reactivex.Single

/**
 * @author Kashonkov Nikita
 */
class RepositoryImpl constructor(val currencyApi: CurrencyApi, val responseConverter: ResponseConverter) : Repository {
    override fun getCurrency(currencyName: String): Single<RatesEntity> {
        return currencyApi.getCurrency(currencyName)
            .map { responseConverter.convertToEntity(it) }
            .map { convertToRatesEntity(it) }
    }
}