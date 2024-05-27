package com.example.safe_memodule

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(R.layout.fragment_home1), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val LOCATION_UPDATE_INTERVAL = 10000L // Update interval in milliseconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Initialize location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    updateMapLocation(it)
                } ?: Log.d(TAG, "Location callback returned null location")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Check and request location permission
        if (isLocationPermissionGranted()) {
            initializeMap()
        } else {
            requestLocationPermission()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun initializeMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Enable user's location on the map if permission is granted
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true

            // Get the last known location and update the map
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    updateMapLocation(it)
                } ?: Log.d(TAG, "Last known location is null")
            }

            // Start location updates
            startLocationUpdates()
        } else {
            requestLocationPermission()
        }
    }

    private fun startLocationUpdates() {
        if (isLocationPermissionGranted()) {
            val locationRequest = LocationRequest.create().apply {
                interval = LOCATION_UPDATE_INTERVAL
                fastestInterval = LOCATION_UPDATE_INTERVAL
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            try {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            } catch (ex: SecurityException) {
                Log.e(TAG, "SecurityException while requesting location updates", ex)
            }
        } else {
            Log.d(TAG, "Location permission not granted, cannot start location updates")
        }
    }

    private fun updateMapLocation(location: Location) {
        val userLocation = LatLng(location.latitude, location.longitude)
        map.clear() // Clear existing markers

        // Decode the resource into a Bitmap
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.current_location)

        if (bitmap != null) {
            map.addMarker(
                MarkerOptions()
                    .position(userLocation)
                    .title("You are here")
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
        } else {
            Log.e(TAG, "Failed to decode resource: R.drawable.current_location")
            // Add a marker without custom icon as a fallback
            map.addMarker(
                MarkerOptions()
                    .position(userLocation)
                    .title("You are here")
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, initialize map and start location updates
                initializeMap()
                startLocationUpdates()
            } else {
                Log.d(TAG, "Location permission denied")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop location updates when the fragment is destroyed
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
