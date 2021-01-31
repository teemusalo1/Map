package com.example.location_teht

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Transformations.map
import androidx.preference.PreferenceManager
import com.google.android.gms.location.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView


private lateinit var fusedLocationClient:FusedLocationProviderClient
//private lateinit var locationCallback: LocationCallback

    var lat: Double = 0.0
    var lng: Double = 0.0

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID)
        Configuration.getInstance().load(ctx,
                PreferenceManager.getDefaultSharedPreferences(ctx))
        setContentView(R.layout.activity_main)


        //!!! if Android >= 6, check Permissions at runtime !!!
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling ActivityCompat.requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper())



    }
    var locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            Geocoder(locationResult)
            var map = findViewById<MapView>(R.id.map)
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.setMultiTouchControls(true)
            map.controller.setZoom(9.0)
            lat = locationResult.lastLocation.latitude
            lng = locationResult.lastLocation.longitude
            map.controller.setCenter(GeoPoint(lat, lng))
            for (location in locationResult.locations){
                // Update UI with location data
                // ...
                val textView = findViewById<TextView>(R.id.textView)
                val texticle = "latitude: ${location.latitude} and longitude: ${location.longitude}"

            }
        }
    }

    private fun Geocoder(locationResult: LocationResult?): String? {
        locationResult!!

        for (location in locationResult.locations) {
            val geocoder = Geocoder(this)
            val list = geocoder.getFromLocation(location.latitude ?: 0.0, location.longitude
                    ?: 0.0, 1)

            val textView = findViewById<TextView>(R.id.textView2)
            val butt = findViewById<Button>(R.id.button)

            butt.setOnClickListener()
            {
                textView.text = list[0].getAddressLine(0)


            }
            Log.d("Check", "We made it this far" +list.toString())
            return list[0].getAddressLine(0)
        }
        return locationResult.toString()
    }
}