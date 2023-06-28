package com.nesib.countriesapp.ui.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nesib.countriesapp.base.BaseFragment
import com.nesib.countriesapp.base.ScreenParams
import com.nesib.countriesapp.databinding.FragmentSearchBinding

class SampleFragment : BaseFragment<FragmentSearchBinding, SampleState, SampleViewModel, SampleFragment.Params>() {

    override val viewModel: SampleViewModel?
        get() = null

    data class Params(val sample: String? = null) : ScreenParams

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding {
        TODO("Not yet implemented")
    }

    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun render(state: SampleState) {
        TODO("Not yet implemented")
    }
}