package com.exmpale.currency.data.model

import com.exmpale.currency.domain.entity.UserError

/**
 * @author Kashonkov Nikita
 */
data class RemoteError(override val message: String) : UserError