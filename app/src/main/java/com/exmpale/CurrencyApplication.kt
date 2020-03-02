package com.exmpale

import android.app.Application

/**
 * @author Kashonkov Nikita
 */
class CurrencyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
        initRx()
    }

    private fun initDI(){
        val component = DaggerAppComponent.create()
        AppComponent.set(component)
    }

    private fun initRx(){
    }
}