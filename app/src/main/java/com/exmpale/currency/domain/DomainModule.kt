package com.exmpale.currency.domain

import com.exmpale.currency.domain.repository.Repository
import com.exmpale.currency.domain.usecase.GetCurrencyUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Kashonkov Nikita
 */
@Module
class DomainModule {
    @Provides
    @Singleton
    fun currencyUseCase(repository: Repository): GetCurrencyUseCase = GetCurrencyUseCase(repository)
}