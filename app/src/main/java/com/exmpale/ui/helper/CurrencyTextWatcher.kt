package com.exmpale.ui.helper

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.text.NumberFormat

/**
 * @author Kashonkov Nikita
 */


class CurrencyTextWatcher(val endInputCallback: ((String) -> Unit)?) : TextWatcher {
    override fun afterTextChanged(s: Editable) {
        endInputCallback?.invoke(s.toString())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, p2: Int, p3: Int) {}
}