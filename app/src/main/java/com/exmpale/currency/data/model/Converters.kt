package com.exmpale.currency.data.model

import com.exmpale.currency.domain.entity.CurrencyEntity
import com.exmpale.currency.domain.entity.RateEntity

/**
 * @author Kashonkov Nikita
 */
fun convertToCurrencyEntity(currencyResponse: CurrencyResponse): CurrencyEntity {
    val rates = currencyResponse.rates.map {entry ->
        RateEntity(
            entry.key,
            entry.value
        )
    }.toSet()
    return CurrencyEntity(currencyResponse.baseCurrency, rates)
}