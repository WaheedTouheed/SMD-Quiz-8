package com.example.androidtopics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    private lateinit var locationTextView: TextView
    private lateinit var liteMapView: MapView
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val mapActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val lat = data?.getDoubleExtra("lat", 0.0) ?: 0.0
                val lng = data?.getDoubleExtra("lng", 0.0) ?: 0.0
                
                locationTextView.text = "Selected Location: $lat, $lng"
                updateLiteMap(lat, lng)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        locationTextView = findViewById(R.id.locationTextView)
        liteMapView = findViewById(R.id.liteMapView)
        liteMapView.onCreate(savedInstanceState)

        // Buttons to navigate to different activities
        val localNotificationButton: Button = findViewById(R.id.localNotificationButton)
        val firebaseNotificationButton: Button = findViewById(R.id.firebaseNotificationButton)
        val cameraActivityButton: Button = findViewById(R.id.cameraActivityButton)
        val mediaActivityButton: Button = findViewById(R.id.mediaActivityButton)
        val mapsActivityButton: Button = findViewById(R.id.mapsActivityButton)

        localNotificationButton.setOnClickListener {
            // Log analytics event
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Local Notification Button")
            firebaseAnalytics.logEvent("local_notification_click", bundle)

            val intent = Intent(this, LocalNotificationActivity::class.java)
            startActivity(intent)
        }

        firebaseNotificationButton.setOnClickListener {
            // Log analytics event
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Firebase Notification Button")
            firebaseAnalytics.logEvent("notification_button_click", bundle)
            Log.d("Analytics", "Event logged: notification_button_click")

            val intent = Intent(this, FirebaseNotificationActivity::class.java)
            startActivity(intent)
        }

        cameraActivityButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        mediaActivityButton.setOnClickListener {
            val intent = Intent(this, MediaActivity::class.java)
            startActivity(intent)
        }

        mapsActivityButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            mapActivityResultLauncher.launch(intent)
        }
    }

    private fun updateLiteMap(lat: Double, lng: Double) {
        val latLng = LatLng(lat, lng)
        liteMapView.getMapAsync { googleMap ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(latLng))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    override fun onResume() {
        super.onResume()
        liteMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        liteMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        liteMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        liteMapView.onLowMemory()
    }
}