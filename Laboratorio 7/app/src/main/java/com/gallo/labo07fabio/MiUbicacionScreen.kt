package com.gallo.labo07fabio

import android.Manifest
import android.content.Context
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.*
import com.google.android.gms.location.FusedLocationProviderClient

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MiUbicacionScreen(fusedLocationClient: FusedLocationProviderClient) {
    val context = LocalContext.current

    val fineAccessPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val coarseAccessPermission = rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)

    val locationText = remember { mutableStateOf("Presiona el botón para obtener la ubicación.") }
    val isLoading = remember { mutableStateOf(false) }
    val hasPermission = fineAccessPermission.status.isGranted || coarseAccessPermission.status.isGranted

    // Intentar obtener ubicación automáticamente si ya se tienen permisos
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            isLoading.value = true
            obtenerUbicacionActual(context, fusedLocationClient) { lat, lon ->
                locationText.value = if (lat != null && lon != null) {
                    "Latitud: $lat\nLongitud: $lon"
                } else {
                    "No se pudo obtener la ubicación."
                }
                isLoading.value = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Mi Ubicación",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = locationText.value,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (!hasPermission) {
                Button(
                    onClick = {
                        // Se piden ambos permisos
                        coarseAccessPermission.launchPermissionRequest()
                        fineAccessPermission.launchPermissionRequest()
                    }
                ) {
                    Text("Solicitar Permiso")
                }

                Text(
                    text = "El permiso es necesario para obtener tu ubicación.",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                Button(
                    onClick = {
                        isLoading.value = true
                        obtenerUbicacionActual(context, fusedLocationClient) { lat, lon ->
                            locationText.value = if (lat != null && lon != null) {
                                "Latitud: $lat\nLongitud: $lon"
                            } else {
                                "No se pudo obtener la ubicación."
                            }
                            isLoading.value = false
                        }
                    }
                ) {
                    Text("Obtener Ubicación")
                }

                if (isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                    Text(
                        text = "Obteniendo ubicación...",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

fun obtenerUbicacionActual(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    callback: (Double?, Double?) -> Unit
) {
    try {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    callback(location.latitude, location.longitude)
                } else {
                    callback(null, null)
                }
            }
            .addOnFailureListener {
                callback(null, null)
            }
    } catch (e: SecurityException) {
        e.printStackTrace()
        callback(null, null)
    }
}
