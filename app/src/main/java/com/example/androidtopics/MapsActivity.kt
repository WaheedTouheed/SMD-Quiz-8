package com.example.androidtopics

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private var selectedLatLng: LatLng? = null
    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        findViewById<Button>(R.id.confirmButton).setOnClickListener {
            selectedLatLng?.let { latLng ->
                val resultIntent = Intent()
                resultIntent.putExtra("lat", latLng.latitude)
                resultIntent.putExtra("lng", latLng.longitude)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val defaultLocation = LatLng(31.5204, 74.3587) // Lahore
        selectedLatLng = defaultLocation
        marker = googleMap.addMarker(MarkerOptions().position(defaultLocation).title("Selected Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))

        googleMap.setOnMapClickListener { latLng ->
            selectedLatLng = latLng
            marker?.position = latLng
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}