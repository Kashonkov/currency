package com.exmpale.ui.helper

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.exmpale.currency.BuildConfig
import java.lang.ref.WeakReference
import java.text.NumberFormat

/**
 * @author Kashonkov Nikita
 */
private const val DEFAULT_VALUE = 0.0
class CurrencyTextWatcher(val endInputCallback: ((Double) -> Unit)?) : TextWatcher {
    private lateinit var editTextWeakReferences: WeakReference<EditText>
    var editText: EditText? = null
        set(value) {
            value?.let {
                editTextWeakReferences = WeakReference(value)
            }
        }

    var current = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (!s.toString().equals(current)) {

            val editText = editTextWeakReferences.get() ?: return
            editText.removeTextChangedListener(this)
            val cleanString: String = s.toString().replace("[$,.\\s]".toRegex(), "")

            val parsed = try {
                cleanString.toDouble() / 100
            } catch (e: Exception) {
                DEFAULT_VALUE
            }

            if (parsed > BuildConfig.MAX_CONVERTED_VALUE) {
                editText.setText(current)
                editText.setSelection(current.length)
                editText.addTextChangedListener(this)
                return
            }

            val formatted = try {
                NumberFormat.getCurrencyInstance().format(parsed)
                    .replace(NumberFormat.getCurrencyInstance().currency.getSymbol(), "").trim()
            } catch (e: Exception) {
                DEFAULT_VALUE.toString()
            }
            current = formatted

            editText.setText(formatted)
            editText.setSelection(formatted.length)
            editText.addTextChangedListener(this)
            endInputCallback?.invoke(parsed)
        }
    }
}