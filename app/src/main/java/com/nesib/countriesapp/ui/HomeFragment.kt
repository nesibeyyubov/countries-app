package com.nesib.countriesapp.ui

import android.widget.RelativeLayout
import androidx.navigation.NavController
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.nesib.countriesapp.R

class HomeFragment : Fragment(), View.OnClickListener {
    private var europa: RelativeLayout? = null
    private var asia: RelativeLayout? = null
    private var africa: RelativeLayout? = null
    private var oceania: RelativeLayout? = null
    private var america: RelativeLayout? = null
    private var navController: NavController? = null
    private var action: HomeFragmentDirections.ActionNavigationHomeToResultsFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        europa = view.findViewById(R.id.europa)
        asia = view.findViewById(R.id.asia)
        africa = view.findViewById(R.id.africa)
        oceania = view.findViewById(R.id.oceania)
        america = view.findViewById(R.id.america)
        navController = Navigation.findNavController(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupClickListeners()
        action = HomeFragmentDirections.actionNavigationHomeToResultsFragment()
    }

    private fun setupClickListeners() {
        europa!!.setOnClickListener(this)
        asia!!.setOnClickListener(this)
        africa!!.setOnClickListener(this)
        oceania!!.setOnClickListener(this)
        america!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.europa -> action!!.setRegionName("Europe")
            R.id.asia -> action!!.setRegionName("Asia")
            R.id.africa -> action!!.setRegionName("Africa")
            R.id.america -> action!!.setRegionName("Americas")
            R.id.oceania -> action!!.setRegionName("Oceania")
        }
        navController!!.navigate(action!!)
    }
}