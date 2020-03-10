package com.exmpale.currency.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmpale.currency.BuildConfig
import com.exmpale.currency.domain.entity.RatesEntity
import com.exmpale.currency.domain.usecase.GetCurrencyUseCase
import com.exmpale.currency.presentation.model.Currency
import com.exmpale.helper.converterScheduler
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Kashonkov Nikita
 */
const val PERIOD_IN_SECONDS = 1L

class CurrencyViewModel @Inject constructor(val useCase: GetCurrencyUseCase) : ViewModel() {

    private val _errorsStream = MutableLiveData<String?>()
    val errorsStream: LiveData<String?>
        get() = _errorsStream

    private val _currenciesStream = MutableLiveData<List<Currency>>()
    val currenciesStream: LiveData<List<Currency>>
        get() = _currenciesStream

    private var currencySubscription = Disposables.disposed()
    private var currentBaseCurrency: Currency? = null
    private var currentInputtedValue = 0.0
    private var currencies: List<Currency>? = null
    private var currentRates: RatesEntity? = null

    init {
        getCurrency(BuildConfig.BASE_CURRENCY)
    }

    override fun onCleared() {
        currencySubscription.dispose()
        super.onCleared()
    }

    fun onValueChange(value: Double) = changeInputtedValue(value)

    fun onBaseCurrencySelected(currencyPosition: Int) {
        changeBaseCurrency(currencyPosition)
    }

    private fun getCurrency(currencyName: String) {
        currencySubscription =
            Observable.interval(PERIOD_IN_SECONDS, TimeUnit.SECONDS, Schedulers.io())
                .flatMapSingle {
                    useCase.getCurrency(
                        currencyName
                    )
                }
                .subscribeOn(Schedulers.io())
                .retry()
                .observeOn(converterScheduler)
                .doOnNext { result ->
                    if (result.isSuccessful) {
                        val ratesEntity = result.data
                        if (!ratesEntity!!.isCurrenciesEquals(currentRates)) {
                            currentRates = ratesEntity
                            createNewCurrencies(result.data)
                        } else {
                            currentRates = ratesEntity
                            countCurrencyValues()
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        _errorsStream.postValue(null)
                    } else {
                        result.error?.let { error ->
                            _errorsStream.postValue(error.message)
                        }
                    }
                }, { error ->
                    error.localizedMessage?.let {
                        _errorsStream.postValue(error.localizedMessage)
                    }
                })
    }

    private fun createNewCurrencies(rates: RatesEntity) {
        val currencyList = mutableListOf<Currency>()

        if (currentBaseCurrency == null || currentBaseCurrency!!.name == rates.baseCurrencyRate.name ||
            (currentBaseCurrency!!.name != rates.baseCurrencyRate.name && (rates.currenciesRates == null || !rates.currenciesRates.containsKey(
                currentBaseCurrency!!.name
            )))
        ) {

            currentBaseCurrency = Currency(
                rates.baseCurrencyRate.name,
                true,
                BehaviorSubject.createDefault(currentInputtedValue)
            )
            currencyList.add(currentBaseCurrency!!)
            rates.currenciesRates?.let {
                currencyList.addAll(it.values.map { currencyRate ->
                    Currency(
                        currencyRate.name,
                        false,
                        BehaviorSubject.createDefault(countValue(currencyRate.rate))
                    )
                })
            }
        } else {
            currentBaseCurrency = Currency(
                currentBaseCurrency!!.name,
                true,
                BehaviorSubject.createDefault(currentInputtedValue)
            )
            currencyList.add(currentBaseCurrency!!)

            val selectedCurrencyRate = rates.currenciesRates!![currentBaseCurrency!!.name]!!.rate
            currencyList.add(
                Currency(
                    rates.baseCurrencyRate.name,
                    false,
                    BehaviorSubject.createDefault(currentInputtedValue / selectedCurrencyRate)
                )
            )

            rates.currenciesRates.values.forEach { currencyRate ->
                if (currencyRate.name != currentBaseCurrency!!.name) {
                    currencyList.add(
                        Currency(
                            currencyRate.name,
                            false,
                            BehaviorSubject.createDefault(
                                countValueCross(
                                    selectedCurrencyRate,
                                    currencyRate.rate
                                )
                            )
                        )
                    )
                }
            }
        }
        currencies = currencyList
        _currenciesStream.postValue(currencies!!)
    }

    private fun countCurrencyValues() {
        if (currentBaseCurrency!!.name != currentRates?.baseCurrencyRate?.name) {
            countCurrencyValuesUsingCrosses()
        } else {
            currencies!!.forEach { currency ->
                if (currency.isBaseCurrency) {
                    currency.value.onNext(currentInputtedValue)
                } else {
                    currentRates!!.currenciesRates?.let {
                        currency.value.onNext(countValue(it[currency.name]?.rate ?: 0.0))
                    }
                }
            }
        }
    }

    private fun countCurrencyValuesUsingCrosses() {
        val baseCurrencyRate = currentRates!!.currenciesRates!![currentBaseCurrency!!.name]!!.rate
        currencies!!.forEach { currency ->
            if (currency.isBaseCurrency) {
                currency.value.onNext(currentInputtedValue)
            } else if (currency.name == currentRates!!.baseCurrencyRate.name) {
                currency.value.onNext(currentInputtedValue / baseCurrencyRate)
            } else {
                currentRates!!.currenciesRates?.let {
                    currency.value.onNext(
                        countValueCross(
                            baseCurrencyRate,
                            it[currency.name]?.rate ?: 0.0
                        )
                    )
                }
            }
        }
    }

    private fun reorderCurrencies(
        swappedPosition: Int,
        oldBase: Currency,
        newBase: Currency
    ): List<Currency> {
        val newCurrencies = mutableListOf<Currency>()
        newCurrencies.add(newBase)
        newCurrencies.add(oldBase)
        for (i in currencies!!.indices) {
            if (i != 0 && i != swappedPosition) {
                newCurrencies.add(currencies!![i])
            }
        }
        return newCurrencies
    }

    private fun changeBaseCurrency(currencyPosition: Int) = Single.fromCallable {
        if (currentBaseCurrency === currencies!![currencyPosition]) return@fromCallable false
        currencySubscription.dispose()
        val oldBaseCurrency = currentBaseCurrency!!.copy(isBaseCurrency = false)
        val newBaseCurrency = currencies!![currencyPosition].copy(isBaseCurrency = true)
        currencies = reorderCurrencies(currencyPosition, oldBaseCurrency, newBaseCurrency)
        currentBaseCurrency = newBaseCurrency
        currentInputtedValue = currentBaseCurrency!!.value.value
        getCurrency(currentBaseCurrency!!.name)
        return@fromCallable true
    }.subscribeOn(converterScheduler)
        .subscribe { shouldRenew ->
            if (shouldRenew) {
                _currenciesStream.postValue(currencies)
            }
        }

    private fun changeInputtedValue(value: Double) {
        Completable.fromCallable {
            currentInputtedValue = value
            if (this.currencies != null) {
                countCurrencyValues()
            }
        }.subscribeOn(converterScheduler)
            .subscribe()
    }

    private fun countValue(rate: Double) = currentInputtedValue * rate

    private fun countValueCross(crossRate: Double, rate: Double) =
        currentInputtedValue / crossRate * rate
}
