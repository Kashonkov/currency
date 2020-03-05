package com.exmpale.currency.api

import com.exmpale.currency.data.model.CurrencyResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author Kashonkov Nikita
 */
interface CurrencyApi {
    @GET("latest?base=EUR")
    fun getCurrency(): Single<Response<CurrencyResponse>>
}