package com.exmpale.helper

import com.exmpale.api.ApiException
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.functions.Consumer
import timber.log.Timber
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * @author Kashonkov Nikita
 */
class RxErrorHandler : Consumer<Throwable> {
    override fun accept(throwable: Throwable) {
        val original: Throwable
        if (throwable is UndeliverableException) {
            original = throwable.cause!!
        } else {
            original = throwable
        }
        if (original is UnknownHostException) {
            Timber.e("skipping UnknownHostException")
        } else if (original is ConnectException) {
            Timber.e("skipping ConnectException")
        } else if (original is SSLHandshakeException) {
            Timber.e("skipping SSLHandshakeException")
        } else if (original is SocketTimeoutException) {
            Timber.e("skipping SocketTimeoutException")
        } else if (original is ApiException) {
            Timber.e("skipping ApiException")
        } else {
            throw RuntimeException(throwable)
        }
    }
}