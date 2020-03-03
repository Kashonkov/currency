package com.exmpale.currency.domain.helper

import com.exmpale.currency.domain.entity.UserError

/**
 * @author Kashonkov Nikita
 */
interface ErrorHandler {
    fun handle(e: Throwable): UserError
}