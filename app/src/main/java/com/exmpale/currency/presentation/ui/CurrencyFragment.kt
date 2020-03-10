package com.exmpale.currency.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.exmpale.AppComponent
import com.exmpale.currency.R
import com.exmpale.currency.presentation.adapter.CurrencyAdapter
import com.exmpale.currency.presentation.adapter.LastItemDivider
import com.exmpale.currency.presentation.model.Currency
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_currency.*
import javax.inject.Inject

/**
 * @author Kashonkov Nikita
 */
class CurrencyFragment : Fragment() {
    //Region DI
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val component by lazy {
        DaggerCurrencyComponent.builder().appComponent(AppComponent.get()).build()
    }
    //end Region

    //region View
    private var snackbar: Snackbar? = null
    //end Region

    //region Other
    private val viewModel: CurrencyViewModel by viewModels { viewModelFactory }
    private val currencyAdapter: CurrencyAdapter by lazy {
        CurrencyAdapter().apply {
            valueChangeListener = viewModel::onValueChange
            itemClickListener = viewModel::onBaseCurrencySelected
        }
    }
    private lateinit var linearManager: LinearLayoutManager
    //end Region

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_currency, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearManager = LinearLayoutManager(requireContext())
        recycler.apply {
            layoutManager = linearManager
            adapter = currencyAdapter
            addItemDecoration(LastItemDivider())
        }
        initObservers()
    }

    override fun onDestroyView() {
        currencyAdapter.onParentDestroy()
        super.onDestroyView()
    }

    private fun initObservers() {
        viewModel.errorsStream.observe(viewLifecycleOwner, errorObserver)
        viewModel.currenciesStream.observe(viewLifecycleOwner, dataObserver)
    }

    private val errorObserver = Observer<String?> { message ->
        if (message.isNullOrEmpty()) {
            if (snackbar != null && snackbar!!.isShownOrQueued) {
                snackbar!!.dismiss()
                snackbar = null
            }
        } else {
            if (snackbar != null && snackbar!!.isShownOrQueued) {
                snackbar!!.setText(message)
            } else {
                snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
                snackbar!!.show()
            }
        }
    }

    private val dataObserver = Observer<List<Currency>> { rates ->
        currencyAdapter.setItems(rates)
        if(rates.isNotEmpty()) {
            linearManager.scrollToPosition(0)
        }
    }

    companion object {
        val FRAGMENT_TAG
            get() = CurrencyFragment::class.simpleName

        fun newInstance() = CurrencyFragment()
    }
}