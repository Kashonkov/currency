package com.exmpale.currency.domain.entity

/**
 * @author Kashonkov Nikita
 */
data class CurrencyEntity(
    val baseCurrency: String,
    val rates: Set<RateEntity>
)

data class RateEntity(
    val name: String,
    val rate: Double
)