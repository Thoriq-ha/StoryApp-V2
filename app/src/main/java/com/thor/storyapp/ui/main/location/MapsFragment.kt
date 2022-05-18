package com.thor.storyapp.ui.main.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.thor.storyapp.R
import com.thor.storyapp.data.preferences.datastore.DataStoreSession
import com.thor.storyapp.utils.vectorToBitmap
import org.koin.android.ext.android.inject

class MapsFragment : Fragment(), OnMapReadyCallback {
    private val viewModel: MapsViewModel by inject()
    private val session: DataStoreSession by inject()

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocation: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())
        session.userFlow.asLiveData().observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.setToken("Bearer ${it.token}")
            }
            viewModel.refresh()
        }
        viewModel.stateList.observe(viewLifecycleOwner, observerStateList)

        mapFragment?.getMapAsync(this)
    }

    private val observerStateList = Observer<MapsState> { itState ->
        when (itState) {
            is MapsState.OnSuccess -> {
                itState.list.forEach { story ->
                    if (story.lat != null && story.lon != null) {
                        val latLng = LatLng(story.lat, story.lon)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(story.name)
                                .snippet("Lat: ${story.lat}, Lon: ${story.lon}")
                                .icon(vectorToBitmap(R.drawable.ic_story_pin, resources))
                        )
                    }
                }
            }
            is MapsState.OnError -> {
                if (!viewModel.token.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), itState.message, Toast.LENGTH_LONG).show()
                }
            }
            MapsState.OnLoading -> {}
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        getMyLocation()
        setMapStyle()
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocation.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f))
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.active_your_location),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.style_map
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
}

