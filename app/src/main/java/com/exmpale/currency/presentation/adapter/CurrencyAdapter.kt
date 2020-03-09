package com.exmpale.currency.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LifecycleObserver
import com.exmpale.currency.R
import com.exmpale.currency.presentation.model.Currency
import com.exmpale.estensions.toFormatedString
import com.exmpale.ui.base.BaseRecyclerAdapter
import com.exmpale.ui.helper.CurrencyTextWatcher
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import timber.log.Timber

/**
 * @author Kashonkov Nikita
 */
class CurrencyAdapter :
    BaseRecyclerAdapter<Currency, CurrencyAdapter.CurrencyViewHolder>(), LifecycleObserver {

    var itemClickListener: ((Int) -> Unit)? = null
    var valueChangeListener: ((String) -> Unit)? = null
    val viewHolderDisposables = CompositeDisposable()
    val baseTextWatcher: CurrencyTextWatcher by lazy { CurrencyTextWatcher(valueChangeListener) }

    fun onParentDestroy() {
        viewHolderDisposables.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        val viewHolder = CurrencyViewHolder(view)
        Timber.i("${this@CurrencyAdapter} create viewHolder ${viewHolder}")
        return viewHolder
    }

    override fun getDiffCallBack(
        oldItems: List<Currency>,
        newItems: List<Currency>
    ) = CurrencyDiffUtils(oldItems, newItems)

    private fun riseItem(position: Int) {
        itemClickListener?.invoke(position)
    }

    inner class CurrencyViewHolder(itemView: View) :
        BaseRecyclerAdapter.ViewHolder<Currency>(itemView) {
        private val currencyName: TextView = itemView.findViewById(R.id.currency_name)
        private val currencyValue: EditText = itemView.findViewById(R.id.currency_value)
        private var changeValueDisposable = Disposables.disposed()
        private var changeBaseCurrencyDisposable = Disposables.disposed()

        init {
            itemView.setOnClickListener {
                riseItem(adapterPosition)
            }
        }

        override fun bindHolder(model: Currency) {
            currencyName.text = model.name

            changeBaseCurrencyDisposable = model.isBaseCurrency.subscribe {isBaseCurrency ->
                currencyValue.isEnabled = isBaseCurrency

                if (isBaseCurrency) {
                    viewHolderDisposables.remove(changeValueDisposable)

                    currencyValue.setText(model.value.value.toFormatedString())
                    currencyValue.addTextChangedListener(baseTextWatcher)

                } else {
                    currencyValue.removeTextChangedListener(baseTextWatcher)
                    changeValueDisposable =
                        model.value.subscribe {
                            currencyValue.setText(it.toFormatedString())
                        }
                    viewHolderDisposables.add(changeValueDisposable)
                }
            }
            viewHolderDisposables.add(changeBaseCurrencyDisposable)

        }

        override fun unbindHolder() {
            viewHolderDisposables.remove(changeValueDisposable)
            viewHolderDisposables.remove(changeBaseCurrencyDisposable)
        }
    }
}