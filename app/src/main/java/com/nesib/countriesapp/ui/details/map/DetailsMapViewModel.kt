package com.nesib.countriesapp.ui.details.map

import com.nesib.countriesapp.base.BaseViewModel

class DetailsMapViewModel : BaseViewModel<DetailsMapState>(DetailsMapState()) {


    fun mapTypeClicked(mapType: MapType) {
        setState { it.copy(mapType = mapType) }
    }


}