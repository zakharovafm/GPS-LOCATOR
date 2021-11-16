package com.example.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi

class LocationHelper : LocationListener {

    private var locationManager: LocationManager? = null
    private var locationUpdater: ((Location) -> Unit)? = null

    companion object{
        private var instance: LocationHelper? = null
        fun getInstance() = instance ?: LocationHelper().also { instance = it }
    }

    override fun onLocationChanged(location: Location) {
        locationUpdater?.invoke(location)
    }

    fun stopLocating(){
        locationManager?.removeUpdates(this)
    }

    fun startLocationListening(context: Context, locationUpdater: (Location) -> Unit) {
        locationManager = (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        if (
            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            this.locationUpdater = locationUpdater
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                0F,
                this
            )
        }
    }
}