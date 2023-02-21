package com.nesib.countriesapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<
        VB : ViewBinding,
        STATE : State,
        VM : BaseViewModel<STATE>,
        PARAMS : ScreenParams> : Fragment() {

    companion object {
        const val KEY_PARAMS = "SCREEN_PARAMS"
    }

    private var params: PARAMS? = null

    private var _binding: VB? = null
    private val binding: VB
        get() = _binding!!


    abstract val viewModel: VM

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleScreenParams()
        initViews()
        observeState()
    }

    private fun observeState() {
        viewModel.state
            .onEach(this::render)
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(lifecycleScope)
    }

    private fun handleScreenParams() {
        val bundle = arguments?.getBundle(KEY_PARAMS)
        params = bundle?.getParcelable(KEY_PARAMS)
    }

    fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun snackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    fun navigate(screenId: Int, params: PARAMS? = null) {
        if (params != null) {
            val bundle = Bundle()
            bundle.putParcelable(KEY_PARAMS, params)
            findNavController().navigate(screenId, bundle)
        } else {
            findNavController().navigate(screenId)
        }
    }

    abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun initViews()

    abstract fun render(state: STATE)

}