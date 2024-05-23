package com.example.mysubmissionintermediate.view.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mysubmissionintermediate.R
import com.example.mysubmissionintermediate.data.ResultState

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mysubmissionintermediate.databinding.ActivityMapsBinding
import com.example.mysubmissionintermediate.view.ViewModelFactory
import com.example.mysubmissionintermediate.view.adapter.StoryAdapter
import com.example.mysubmissionintermediate.view.main.MainViewModel
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        addManyMarker()
    }

    private fun addManyMarker() {
        viewModel.getStoryLocation().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        var hasIndonesianLocation = false

                        result.data.listStory.forEach { data ->
                            val lat = data?.lat
                            val lon = data?.lon

                            // Define bounds for Indonesia
                            val minLat = -11.0 // Southernmost point of Indonesia
                            val maxLat = 6.0   // Northernmost point of Indonesia
                            val minLon = 95.0  // Westernmost point of Indonesia
                            val maxLon = 141.0 // Easternmost point of Indonesia

                            // Check if the location is within Indonesia's bounds
                            if (lat != null && lon != null && lat in minLat..maxLat && lon in minLon..maxLon) {
                                val latLng = LatLng(lat, lon)
                                mMap.addMarker(MarkerOptions().position(latLng).title(data.name))
                                boundsBuilder.include(latLng)
                            }
                        }
                        if (hasIndonesianLocation) {
                            val bounds: LatLngBounds = boundsBuilder.build()
                            mMap.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                    bounds,
                                    resources.displayMetrics.widthPixels,
                                    resources.displayMetrics.heightPixels,
                                    2000
                                )
                            )
                        } else {
                            // Define central point of Indonesia
                            val indonesiaCenter = LatLng(-2.5, 118.0)
                            val zoomLevel = 3.0f // Adjust zoom level as needed
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indonesiaCenter, zoomLevel))
                        }

                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}