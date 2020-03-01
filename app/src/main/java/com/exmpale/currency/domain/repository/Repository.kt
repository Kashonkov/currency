package com.exmpale.currency.domain.repository

import com.exmpale.currency.domain.entity.CurrencyEntity
import io.reactivex.Single

/**
 * @author Kashonkov Nikita
 */
interface Repository {
    fun getCurrency(): Single<CurrencyEntity>
}