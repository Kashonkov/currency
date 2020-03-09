package com.exmpale.currency.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exmpale.currency.BuildConfig
import com.exmpale.currency.domain.entity.RatesEntity
import com.exmpale.currency.domain.usecase.GetCurrencyUseCase
import com.exmpale.currency.presentation.model.Currency
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * @author Kashonkov Nikita
 */
const val PERIOD_IN_SECONDS = 1L

class CurrencyViewModel @Inject constructor(val useCase: GetCurrencyUseCase) : ViewModel() {
    private var currencySubscription = Disposables.disposed()

    private var currentBaseCurrency: Currency? = null

    private var currentInputedValue = 228.30

    private val _errors = MutableLiveData<String?>()
    val errors: LiveData<String?>
        get() = _errors

    private val _currencies = MutableLiveData<List<Currency>>()
    val currencies: LiveData<List<Currency>>
        get() = _currencies

    private var currenciesData: MutableList<Currency>? = null
    private var currentRates: RatesEntity? = null

    init {
        getCurrency()
    }

    override fun onCleared() {
        currencySubscription.dispose()
        super.onCleared()
    }

    fun onValueChange(value: String) {
        val format: NumberFormat = NumberFormat.getInstance()
        val number: Number? = format.parse(value)
        currentInputedValue = number?.toDouble() ?: 0.0
        if (this.currenciesData != null) {
            countCurrencyValues()
        }
    }

    fun onBaseCurrencySelected(currencyPosition: Int) {
        currencySubscription.dispose()
//        currentBaseCurrency!!.isBaseCurrency.onNext(false)
//        currentBaseCurrency = currenciesData!![currencyPosition]
//        currentInputedValue = currentBaseCurrency!!.value.value
//        currentBaseCurrency!!.isBaseCurrency.onNext(true)
        reorderCurrencies(currencyPosition)
        _currencies.postValue(currenciesData)
        getCurrency()
    }

    private fun getCurrency() {
        currencySubscription =
            Observable.interval(PERIOD_IN_SECONDS, TimeUnit.SECONDS, Schedulers.io())
                .flatMapSingle {
                    useCase.getCurrency(
                        currentBaseCurrency?.name ?: BuildConfig.BASE_CURRENCY
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe({ result ->
                    if (result.isSuccessful) {
                        _errors.postValue(null)
                        val ratesEntity = result.data
                        if (!ratesEntity!!.isCurrenciesEquals(currentRates)) {
                            currentRates = ratesEntity
                            createNewCurrencyData(result.data)
                        } else {
                            currentRates = ratesEntity
                            countCurrencyValues()
                        }

                        Timber.i(result.data.baseCurrencyRate.name)
                    } else {
                        result.error?.let { error ->
                            _errors.postValue(error.message)
                        }
                        Timber.i(result.error?.message)
                    }
                }, { error ->
                    error.localizedMessage?.let {
                        _errors.postValue(error.localizedMessage)
                    }
                    Timber.i(error.localizedMessage)
                })
    }

    private fun createNewCurrencyData(rates: RatesEntity) {
        val currencyList = mutableListOf<Currency>()

        if (currentBaseCurrency == null || currentBaseCurrency!!.name == rates.baseCurrencyRate.name ||
            (currentBaseCurrency!!.name != rates.baseCurrencyRate.name && (rates.currenciesRates == null || !rates.currenciesRates.containsKey(
                currentBaseCurrency!!.name
            )))
        ) {

            currentBaseCurrency = Currency(
                rates.baseCurrencyRate.name,
                BehaviorSubject.createDefault(true),
                BehaviorSubject.createDefault(currentInputedValue)
            )
            currencyList.add(currentBaseCurrency!!)
            rates.currenciesRates?.let {
                currencyList.addAll(it.values.map { currencyRate ->
                    Currency(
                        currencyRate.name,
                        BehaviorSubject.createDefault(false),
                        BehaviorSubject.createDefault(countValue(currencyRate.rate))
                    )
                })
            }
        } else {
            currentBaseCurrency = Currency(
                currentBaseCurrency!!.name,
                BehaviorSubject.createDefault(true),
                BehaviorSubject.createDefault(currentInputedValue)
            )
            currencyList.add(currentBaseCurrency!!)

            val selectedCurrencyRate = rates.currenciesRates!![currentBaseCurrency!!.name]!!.rate
            currencyList.add(
                Currency(
                    rates.baseCurrencyRate.name,
                    BehaviorSubject.createDefault(false),
                    BehaviorSubject.createDefault(currentInputedValue / selectedCurrencyRate)
                )
            )

            rates.currenciesRates.values.forEach { currencyRate ->
                if (currencyRate.name != currentBaseCurrency!!.name) {
                    currencyList.add(
                        Currency(
                            currencyRate.name,
                            BehaviorSubject.createDefault(false),
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
        currenciesData = currencyList
        _currencies.postValue(currenciesData!!)
    }

    private fun countCurrencyValues() {
        if (currentBaseCurrency!!.name != currentRates?.baseCurrencyRate?.name) {
            countCurrencyValuesUsingCrosses()
        } else {
            currenciesData!!.forEach { currency ->
                if (currency.isBaseCurrency.value) {
                    currency.value.onNext(currentInputedValue)
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
        currenciesData!!.forEach { currency ->
            if (currency.isBaseCurrency.value) {
                currency.value.onNext(currentInputedValue)
            } else if (currency.name == currentRates!!.baseCurrencyRate.name) {
                currency.value.onNext(currentInputedValue / baseCurrencyRate)
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

    private fun reorderCurrencies(swappedPosition: Int){
        for (i in swappedPosition downTo 1){
            Collections.swap(currenciesData!!, i, i-1)
        }
        val y = 1+1
    }
    private fun countValue(rate: Double) = currentInputedValue * rate
    private fun countValueCross(crossRate: Double, rate: Double) =
        currentInputedValue / crossRate * rate

    private val converterScheduler = Schedulers.from(
        ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            LinkedBlockingQueue<Runnable>(),
            ThreadFactory {
                Thread(it)
            })
    )
}