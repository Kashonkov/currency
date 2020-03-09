package com.exmpale.currency.domain.entity

import com.exmpale.currency.BuildConfig

/**
 * @author Kashonkov Nikita
 */
data class RatesEntity(
    val baseCurrencyRate: CurrencyRateEntity,
    val currenciesRates: Map<String, CurrencyRateEntity>?,
    val currenciesSet: MutableSet<String>
){
    fun isCurrenciesEquals(ratesEntity: RatesEntity?): Boolean{
        if(ratesEntity == null) return false
        return currenciesSet == ratesEntity.currenciesSet
    }
}

data class CurrencyRateEntity(
    val name: String,
    val rate: Double,
    var isBaseCurrency: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CurrencyRateEntity

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}
