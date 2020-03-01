package com.exmpale.api

/**
 * @author Kashonkov Nikita
 */
class ApiException : Exception {
    /**
     * Http статус ответа
     */
    val statusCode: Int
    /**
     * Ошибка API
     */
    val apiError: ApiError?

    constructor(statusCode: Int) {
        this.statusCode = statusCode
        this.apiError = null
    }

    constructor(cause: Throwable, statusCode: Int) : super(cause) {
        this.statusCode = statusCode
        this.apiError = null
    }

    constructor(message: String, statusCode: Int) : super(message) {
        this.statusCode = statusCode
        this.apiError = null
    }

    constructor (statusCode: Int, apiError: ApiError) {
        this.statusCode = statusCode
        this.apiError = apiError
    }

}
