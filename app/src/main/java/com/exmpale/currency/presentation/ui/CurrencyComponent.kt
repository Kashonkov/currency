package com.exmpale.currency.presentation.ui

import com.exmpale.AppComponent
import com.exmpale.dagger.FragmentScope
import dagger.Component

/**
 * @author Kashonkov Nikita
 */
@FragmentScope
@Component(dependencies = [AppComponent::class])
interface CurrencyComponent {
    fun inject(fragment: CurrencyFragment)
}