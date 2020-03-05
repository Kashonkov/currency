package com.exmpale.currency.data.helper

import android.content.Context
import com.exmpale.api.ApiException
import com.exmpale.currency.R
import com.exmpale.currency.data.model.RemoteError
import com.exmpale.currency.domain.entity.UserError
import com.exmpale.currency.domain.helper.ErrorHandler
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author Kashonkov Nikita
 */
class ErrorHandlerImpl(context: Context) : ErrorHandler {
    private val serverUnavailableMsg = context.getString(R.string.error_connect)

    private val serverErrorMsg = context.getString(R.string.error_server)

    private val timeoutMsg = context.getString(R.string.error_timeout)

    private val noConnectionMsg = context.getString(R.string.error_no_internet_connection)

    private val unknownErrorMsg = context.getString(R.string.error_unknown)

    override fun handle(e: Throwable): UserError {
        return if (e is ApiException) {
            if (e.statusCode in 502..504) {
                RemoteError(serverUnavailableMsg)
            } else {
                val apiError = e.apiError
                return if (apiError == null) {
                    val message = e.message
                    if (message.isNullOrEmpty()) {
                        RemoteError("$serverErrorMsg (${e.statusCode})")
                    } else {
                        RemoteError(message)
                    }
                } else {
                    RemoteError(apiError.message)
                }
            }
        } else if (e is SocketTimeoutException) {
            RemoteError(timeoutMsg)
        } else if (e is ConnectException) {
            RemoteError(serverUnavailableMsg)
        } else if (e is SocketException) {
            RemoteError(noConnectionMsg)
        } else if (e is UnknownHostException) {
            RemoteError(noConnectionMsg)
        } else {
            RemoteError(unknownErrorMsg)
        }
    }
}