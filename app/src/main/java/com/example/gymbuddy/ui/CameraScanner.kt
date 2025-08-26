package com.example.gymbuddy.ui

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import android.widget.Space
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymbuddy.QrCodeAnalyzer
import com.example.gymbuddy.R
import com.example.gymbuddy.ui.theme.ui.theme.schedaTitle
import com.example.gymbuddy.viewModel.AppViewModelProvider
import com.example.gymbuddy.viewModel.CameraScannerViewModel
import com.example.gymbuddy.viewModel.SchedeViewModel

@Composable
fun CameraScanner(
    navController: NavController,
    viewModel: CameraScannerViewModel = viewModel(factory = AppViewModelProvider.Factory),
    themeLight: Boolean,
) {
    val cameraUiState by viewModel.cameraScannerUiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var code by remember {
        mutableStateOf("")
    }
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        })

    LaunchedEffect(key1 = true) {
        if (!hasCamPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }
    if (hasCamPermission) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AndroidView(factory = { context ->
                val previewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector =
                    CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                preview.setSurfaceProvider(previewView.surfaceProvider)
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    QrCodeAnalyzer { result ->
                        Log.v("json", result)
                        viewModel.readSchedaFromQr(result)
                    })
                try {
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner, selector, preview, imageAnalysis
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.v("Camera", "Error")
                }
                previewView
            })
            if (cameraUiState.trovato) {
                Column {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                                )
                                .background(color = Color(0xFFF6F6F6))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                Text(
                                    text = cameraUiState.titolo,
                                    style = schedaTitle
                                )
                                Text(
                                    text = cameraUiState.descrizione,
                                    maxLines = 2,
                                    color= Color.Black,
                                    overflow = TextOverflow.Ellipsis
                                )
                                InfoRow(
                                    icon = painterResource(id = R.drawable.dumbbell_solid),
                                    mainText = "Numero esercizi",
                                    value = cameraUiState.numeroEsercizi.toString()
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    Button(
                        onClick = {
                            viewModel.insertSchedaExport()
                            navController.navigate("ListaSchede")
                        },
                        colors= ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                        ),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .shadow(elevation = 10.dp, shape = RoundedCornerShape(8.dp))
                    ) {
                        Text(text = "Aggiungi scheda")
                    }
                }
            }
            if (cameraUiState.error) {
                Toast.makeText(context, "QR Code invalido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}