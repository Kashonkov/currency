package com.exmpale.currency.domain.usecase

import com.exmpale.currency.domain.entity.CurrencyEntity
import com.exmpale.currency.domain.repository.Repository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author Kashonkov Nikita
 */
class GetCurrencyUseCase(val repository: Repository) {

    fun getCurrency(): Observable<CurrencyEntity> {
        return Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
            .flatMapSingle { _ -> repository.getCurrency() }
    }

}

