package com.exmpale

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import javax.inject.Singleton

/**
 * @author Kashonkov Nikita
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    companion object {
        private lateinit var instance: AppComponent

        fun get(): AppComponent = instance

        fun set(legacyAppComponent: AppComponent) {
            instance = legacyAppComponent
        }
    }
}