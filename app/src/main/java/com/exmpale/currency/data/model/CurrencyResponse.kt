package com.exmpale.currency.data.model

/**
 * @author Kashonkov Nikita
 */
data class CurrencyResponse constructor(
    val baseCurrency: String,
    val rates: Map<String, Double>
)