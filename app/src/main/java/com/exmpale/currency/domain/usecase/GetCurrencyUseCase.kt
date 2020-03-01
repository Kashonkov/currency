package com.exmpale.currency.domain.usecase

import com.exmpale.currency.data.repository.RepositoryImpl
import com.exmpale.currency.domain.entity.CurrencyEntity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author Kashonkov Nikita
 */
class GetCurrencyUseCase {
    fun getCurrency(): Observable<CurrencyEntity> {
        val repository = RepositoryImpl()

        return Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
            .flatMapSingle { _ -> repository.getCurrency() }
    }
}
