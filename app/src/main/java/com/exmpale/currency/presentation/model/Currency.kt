package com.exmpale.currency.presentation.model

import io.reactivex.subjects.BehaviorSubject

/**
 * @author Kashonkov Nikita
 */
data class Currency(
    val name: String,
    val isBaseCurrency: Boolean,
    val value: BehaviorSubject<Double>
)