package com.exmpale.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exmpale.currency.R
import com.exmpale.currency.presentation.ui.CurrencyFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, CurrencyFragment.newInstance())
        transaction.commit()
    }
}
