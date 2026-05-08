package com.udeh.zuru.ui.screens.maps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MapsViewModel : ViewModel() {

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation.asStateFlow()

    private val _touristSites = MutableStateFlow<List<Place>>(emptyList())
    val touristSites: StateFlow<List<Place>> = _touristSites.asStateFlow()

    private val _hotels = MutableStateFlow<List<Place>>(emptyList())
    val hotels: StateFlow<List<Place>> = _hotels.asStateFlow()

    private val _selectedPlace = MutableStateFlow<Place?>(null)
    val selectedPlace: StateFlow<Place?> = _selectedPlace.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var placesClient: PlacesClient? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    fun initialize(context: Context) {
        if (placesClient == null) {
            placesClient = Places.createClient(context)
        }
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchCurrentLocation() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                _currentLocation.value = latLng
                fetchNearbyPlaces(latLng, listOf(Place.Type.TOURIST_ATTRACTION, Place.Type.MUSEUM, Place.Type.PARK))
            }
        }
    }

    private fun fetchNearbyPlaces(location: LatLng, types: List<Place.Type>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val placeFields = listOf(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG,
                    Place.Field.RATING,
                    Place.Field.TYPES,
                    Place.Field.PHOTO_METADATAS,
                    Place.Field.BUSINESS_STATUS,
                    Place.Field.ADDRESS                )

                val circle = CircularBounds.newInstance(location, 5000.0) // 5km radius

                val request = SearchNearbyRequest.builder(circle, placeFields)
                    .setIncludedTypes(types.map { it.name })
                    .setMaxResultCount(10)
                    .build()

                val response = placesClient?.searchNearby(request)?.await()
                
                if (types.contains(Place.Type.TOURIST_ATTRACTION)) {
                    _touristSites.value = response?.places ?: emptyList()
                } else if (types.contains(Place.Type.LODGING)) {
                    _hotels.value = response?.places ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectPlace(place: Place?) {
        _selectedPlace.value = place
    }

    fun findHotelsNear(location: LatLng) {
        fetchNearbyPlaces(location, listOf(Place.Type.LODGING))    }
}
