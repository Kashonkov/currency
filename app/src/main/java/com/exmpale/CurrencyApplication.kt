package com.exmpale

import android.app.Application
import com.exmpale.helper.RxErrorHandler
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

/**
 * @author Kashonkov Nikita
 */
class CurrencyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
        initRx()
        initLogger()
    }

    private fun initDI(){
        val component = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        AppComponent.set(component)
    }

    private fun initRx(){
        RxJavaPlugins.setErrorHandler(RxErrorHandler())
    }

    private fun initLogger(){
        Timber.plant(Timber.DebugTree())
    }
}