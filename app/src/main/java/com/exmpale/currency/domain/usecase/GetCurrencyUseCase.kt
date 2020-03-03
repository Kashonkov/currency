package com.exmpale.currency.domain.usecase

import com.exmpale.currency.domain.entity.CurrencyEntity
import com.exmpale.currency.domain.entity.UserError
import com.exmpale.currency.domain.helper.ErrorHandler
import com.exmpale.currency.domain.repository.Repository
import io.reactivex.Single

/**
 * @author Kashonkov Nikita
 */
class GetCurrencyUseCase(val repository: Repository, val errorHandler: ErrorHandler) {
    fun getCurrency(): Single<GetCurrencyUseCaseResult> {
        return repository.getCurrency()
            .map { entity -> GetCurrencyUseCaseResult(entity, null) }
            .onErrorReturn { error ->
                val userError = errorHandler.handle(error)
                GetCurrencyUseCaseResult(null, userError)
            }

    }
}

class GetCurrencyUseCaseResult(val data: CurrencyEntity?, val error: UserError?) {
    val isSuccessful
        get() = error == null
}

