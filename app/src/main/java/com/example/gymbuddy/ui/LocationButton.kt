package com.example.gymbuddy.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun LocationButton(
    onLocationReceived: (Location) -> Unit
) {
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasLocationPermission = granted
        })

    LaunchedEffect(key1 = true) {
        if (!hasLocationPermission) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    Button(onClick = {
        if (hasLocationPermission) {
            val location = getUserLocation(context)
            if (location != null) {
                onLocationReceived(location)
            }
        }
    }
    ) {
        Icon(imageVector = Icons.Default.Place, contentDescription ="Get position" )
    }

}

private fun getUserLocation(context: Context): Location? {
    val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as? android.location.LocationManager

    return if (locationManager != null) {
        try {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (e: SecurityException) {
            null
        }
    } else {
        null
    }
}