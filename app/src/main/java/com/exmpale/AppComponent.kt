package com.exmpale

import android.content.Context
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
    fun context(): Context

    companion object {
        private lateinit var instance: AppComponent

        fun get(): AppComponent = instance

        fun set(legacyAppComponent: AppComponent) {
            instance = legacyAppComponent
        }
    }
}