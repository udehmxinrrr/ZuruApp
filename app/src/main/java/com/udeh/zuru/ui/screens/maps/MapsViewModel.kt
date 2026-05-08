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
import kotlinx.coroutines.async
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
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val location = fusedLocationClient?.lastLocation?.await()
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    _currentLocation.value = latLng
                    // Fetch tourist attractions and hotels concurrently
                    val touristDeferred = async { fetchPlaces(latLng, listOf(Place.Type.TOURIST_ATTRACTION, Place.Type.MUSEUM, Place.Type.PARK)) }
                    val hotelsDeferred = async { fetchPlaces(latLng, listOf(Place.Type.LODGING)) }
                    
                    _touristSites.value = touristDeferred.await()
                    _hotels.value = hotelsDeferred.await()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchPlaces(location: LatLng, types: List<Place.Type>): List<Place> {
        return try {
            val placeFields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.RATING,
                Place.Field.TYPES,
                Place.Field.PHOTO_METADATAS,
                Place.Field.BUSINESS_STATUS,
                Place.Field.ADDRESS
            )

            val circle = CircularBounds.newInstance(location, 5000.0)

            val request = SearchNearbyRequest.builder(circle, placeFields)
                .setIncludedTypes(types.map { it.name })
                .setMaxResultCount(10)
                .build()

            placesClient?.searchNearby(request)?.await()?.places ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun selectPlace(place: Place?) {
        _selectedPlace.value = place
    }

    fun findHotelsNear(location: LatLng) {
        viewModelScope.launch {
            _isLoading.value = true
            _hotels.value = fetchPlaces(location, listOf(Place.Type.LODGING))
            _isLoading.value = false
        }
    }
}
