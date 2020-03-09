package com.exmpale.currency.data.model

import com.exmpale.currency.domain.entity.CurrencyRateEntity
import com.exmpale.currency.domain.entity.RatesEntity

/**
 * @author Kashonkov Nikita
 */
fun convertToRatesEntity(currencyResponse: RateResponse): RatesEntity {

    val baseCurrency = CurrencyRateEntity(currencyResponse.baseCurrency, 1.0, true)
    val currencySet = mutableSetOf(baseCurrency.name)
    val currencies = currencyResponse.rates?.mapValues { it ->
        currencySet.add(it.key)
        CurrencyRateEntity(it.key, it.value, false)
    }

    return RatesEntity(baseCurrency, currencies, currencySet)
}