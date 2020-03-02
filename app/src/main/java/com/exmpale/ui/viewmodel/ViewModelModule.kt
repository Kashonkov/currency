package com.exmpale.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exmpale.currency.presentation.ui.CurrencyViewModel
import com.exmpale.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author Kashonkov Nikita
 */
@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CurrencyViewModel::class)
    internal abstract fun currencyViewModel(viewModel: CurrencyViewModel): ViewModel

}