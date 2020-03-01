package com.exmpale.api

import com.exmpale.currency.data.model.CurrencyResponse
import io.reactivex.Single
import retrofit2.http.GET

/**
 * @author Kashonkov Nikita
 */
interface CurrencyApi {
    @GET("latest?base=EUR")
    fun getCurrency(): Single<CurrencyResponse>
}