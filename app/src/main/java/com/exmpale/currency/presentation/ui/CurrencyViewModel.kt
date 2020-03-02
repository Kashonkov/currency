package com.exmpale.currency.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exmpale.currency.domain.usecase.GetCurrencyUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * @author Kashonkov Nikita
 */
class CurrencyViewModel @Inject constructor(val useCase: GetCurrencyUseCase): ViewModel() {
    private var currencySubscription = Disposables.disposed()

    private var errorMessage: String? = null
    private var needShowLoader: Boolean = true


    override fun onCleared() {
        currencySubscription.dispose()
        super.onCleared()
    }

    fun getCurrency() {
        currencySubscription = useCase.getCurrency()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError{Log.i("myLog", "${it.javaClass}")}
            .retry()
            .subscribe({ response ->
                Log.i("myLog", "${response.rates.joinToString()}")
            }, { error ->
                Log.i("myLog", "${error.localizedMessage}")
            })
    }
}
