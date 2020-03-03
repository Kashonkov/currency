package com.exmpale.api

import retrofit2.Retrofit
import javax.inject.Inject

/**
 * @author Kashonkov Nikita
 */
class ResponseConverter @Inject internal constructor(private val retrofit: Retrofit) {

    @Throws(ApiException::class)
    fun <E> convertToEntity(retrofitResponse: retrofit2.Response<E>): E {
        val response = convert(retrofitResponse);
        if (response.isSuccessful) {
            return response.data
        } else {
            throw ApiException(response.code, response.error)
        }
    }

    @Throws(ApiException::class)
    fun <E> convert(retrofitResponse: retrofit2.Response<E>): Response<E> {
        if (retrofitResponse.isSuccessful) {
            // Если запрос завершился успешно
            val data = retrofitResponse.body()
            return Response(data, null, retrofitResponse.code())
        } else {
            // Если запрос завершился неудачно
            val responseBody = retrofitResponse.errorBody()
                ?: throw ApiException(retrofitResponse.code())
            try {
                // Пробуем получить тело ответа как строку
                val message = responseBody.string()
                throw ApiException(message, retrofitResponse.code())
            } catch (e: Exception) {
//                logger.error("Error while getting response body as string", e)
                throw ApiException(e, retrofitResponse.code())
            }
        }
    }
}
