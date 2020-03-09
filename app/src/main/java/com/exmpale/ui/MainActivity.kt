package com.exmpale.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exmpale.currency.R
import com.exmpale.currency.presentation.ui.CurrencyFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.findFragmentByTag(CurrencyFragment.FRAGMENT_TAG) == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(
                R.id.fragment_container,
                CurrencyFragment.newInstance(),
                CurrencyFragment.FRAGMENT_TAG
            )
            transaction.commit()
        }
    }
}
