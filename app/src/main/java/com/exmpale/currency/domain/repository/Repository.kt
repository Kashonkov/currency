package com.exmpale.currency.domain.repository

import com.exmpale.currency.domain.entity.RatesEntity
import io.reactivex.Single

/**
 * @author Kashonkov Nikita
 */
interface Repository {
    fun getCurrency(currencyName: String): Single<RatesEntity>
}