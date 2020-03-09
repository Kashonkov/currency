package com.exmpale.currency.domain.usecase

import com.exmpale.currency.domain.entity.RatesEntity
import com.exmpale.currency.domain.entity.UserError
import com.exmpale.currency.domain.helper.ErrorHandler
import com.exmpale.currency.domain.repository.Repository
import io.reactivex.Single

/**
 * @author Kashonkov Nikita
 */
class GetCurrencyUseCase(val repository: Repository, val errorHandler: ErrorHandler) {
    fun getCurrency(currencyName: String): Single<GetCurrencyUseCaseResult> {
        return repository.getCurrency(currencyName)
            .map { entity -> GetCurrencyUseCaseResult(entity, null) }
            .onErrorReturn { error ->
                val userError = errorHandler.handle(error)
                GetCurrencyUseCaseResult(null, userError)
            }

    }
}

class GetCurrencyUseCaseResult(val data: RatesEntity?, val error: UserError?) {
    val isSuccessful
        get() = error == null
}

