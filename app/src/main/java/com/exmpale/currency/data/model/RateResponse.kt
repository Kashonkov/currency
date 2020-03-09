package com.exmpale.currency.data.model

/**
 * @author Kashonkov Nikita
 */
data class RateResponse constructor(
    val baseCurrency: String,
    val rates: Map<String, Double>?
)