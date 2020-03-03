package com.exmpale.currency.domain

import com.exmpale.currency.domain.helper.ErrorHandler
import com.exmpale.currency.domain.repository.Repository
import com.exmpale.currency.domain.usecase.GetCurrencyUseCase
import dagger.Module
import dagger.Provides

/**
 * @author Kashonkov Nikita
 */
@Module
class DomainModule {
    @Provides
    fun currencyUseCase(repository: Repository, errorHandler: ErrorHandler): GetCurrencyUseCase =
        GetCurrencyUseCase(repository, errorHandler)
}