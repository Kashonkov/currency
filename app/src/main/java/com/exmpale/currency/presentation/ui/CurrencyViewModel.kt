package com.exmpale.currency.presentation.ui

import androidx.lifecycle.ViewModel
import com.exmpale.currency.domain.usecase.GetCurrencyUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Kashonkov Nikita
 */
const val PERIOD_IN_SECONDS = 1L
class CurrencyViewModel @Inject constructor(val useCase: GetCurrencyUseCase) : ViewModel() {
    private var currencySubscription = Disposables.disposed()

    private var errorMessage: String? = null
    private var needShowLoader: Boolean = true


    override fun onCleared() {
        currencySubscription.dispose()
        super.onCleared()
    }

    fun getCurrency() {
        currencySubscription = Observable.interval(PERIOD_IN_SECONDS, TimeUnit.SECONDS, Schedulers.io())
            .flatMapSingle { useCase.getCurrency()}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe({ result ->
                if(result.isSuccessful) {
                    Timber.i(result.data!!.rates.joinToString())
                } else {
                    Timber.i("Some error")
                }
            }, { error ->
                Timber.i( error.localizedMessage)
            })
    }
}
