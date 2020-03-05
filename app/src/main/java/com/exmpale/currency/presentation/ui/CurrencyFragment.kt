package com.exmpale.currency.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.exmpale.AppComponent
import com.exmpale.currency.R
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

/**
 * @author Kashonkov Nikita
 */
class CurrencyFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val component by lazy {
        DaggerCurrencyComponent.builder().appComponent(AppComponent.get()).build()
    }

    private val viewModel: CurrencyViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text.setOnClickListener {
            viewModel.getCurrency()
        }
    }

    companion object{
        fun newInstance() = CurrencyFragment()
    }
}