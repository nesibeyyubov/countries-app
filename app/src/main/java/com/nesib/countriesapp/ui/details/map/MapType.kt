package com.nesib.countriesapp.ui.details.map

import com.google.android.gms.maps.GoogleMap

enum class MapType(val value: Int) {
    Normal(GoogleMap.MAP_TYPE_NORMAL),
    Satellite(GoogleMap.MAP_TYPE_SATELLITE),
    Hybrid(GoogleMap.MAP_TYPE_HYBRID),
    Terrain(GoogleMap.MAP_TYPE_TERRAIN),
}