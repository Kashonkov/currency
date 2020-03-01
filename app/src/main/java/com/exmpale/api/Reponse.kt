package com.exmpale.api

/**
 * @author Kashonkov Nikita
 */
class Response<T>(private val _data: T?, private val _error: ApiError?, val code: Int) {
    val data
        get() = _data!!
    val error
        get() = _error!!
    val isSuccessful
        get() = code in 200..299
}

class ApiError(val code: Int, val message: String)