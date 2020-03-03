package com.exmpale

import android.content.Context
import com.exmpale.api.ApiModule
import com.exmpale.currency.data.DataModule
import com.exmpale.currency.domain.DomainModule
import com.exmpale.ui.viewmodel.ViewModelModule
import dagger.Module
import dagger.Provides

/**
 * @author Kashonkov Nikita
 */
@Module(
    includes = [
        ApiModule::class,
        DataModule::class,
        DomainModule::class,
        ViewModelModule::class
    ]
)
class AppModule(val context: Context) {
    @Provides
    fun context() = context
}