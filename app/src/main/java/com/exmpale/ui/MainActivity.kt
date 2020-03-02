package com.exmpale.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.exmpale.AppComponent
import com.exmpale.currency.R
import com.exmpale.currency.presentation.ui.CurrencyViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CurrencyViewModel by viewModels {viewModelFactory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val component = DaggerMainComponent.builder()
            .appComponent(AppComponent.get())
            .build()
        component.inject(this)

        text.setOnClickListener {
            viewModel.getCurrency()
        }
    }
}
