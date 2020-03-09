package com.exmpale.currency.api

import com.exmpale.currency.data.model.RateResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Kashonkov Nikita
 */
interface CurrencyApi {
    @GET("latest")
    fun getCurrency(@Query("base")currencyName: String): Single<Response<RateResponse>>
}