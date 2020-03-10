package com.exmpale.currency.presentation.adapter

import com.exmpale.currency.domain.entity.RatesEntity
import com.exmpale.currency.presentation.model.Currency
import com.exmpale.ui.base.BaseDiffUtilCallback

/**
 * @author Kashonkov Nikita
 */
class CurrencyDiffUtils(newItems: List<Currency>, oldItems: List<Currency>): BaseDiffUtilCallback<Currency>(newItems, oldItems) {
    override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.value === newItem.value && oldItem.isBaseCurrency == newItem.isBaseCurrency
    }
}