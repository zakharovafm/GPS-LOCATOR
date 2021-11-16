package com.example.gps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start.setOnClickListener {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission()
            else {
                manageLocationService()
                tv_service_state.text = "Геопозиция отслеживается"
            }
        }
        btn_stop.setOnClickListener {
            manageLocationService(false)
            tv_service_state.text = "Геопозиция не отслеживается"
        }
    }

    private fun requestLocationPermission() {
        this.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray

    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100
            && permissions.first() == Manifest.permission.ACCESS_FINE_LOCATION
            && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
            manageLocationService()
        }
    }

    private fun manageLocationService(isLocating: Boolean = true) {
        val intent = Intent(this, LocationService::class.java)
        intent.putExtra("isLocating", isLocating)
        startService(intent)
    }
}

