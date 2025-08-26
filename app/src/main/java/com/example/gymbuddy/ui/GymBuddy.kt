@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gymbuddy.ui

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.lights.Light
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.BackpressureStrategy
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gymbuddy.QrCodeAnalyzer
import com.example.gymbuddy.R
import com.example.gymbuddy.data.NavigationRoute
import com.example.gymbuddy.data.Scheda
import com.example.gymbuddy.ui.theme.ui.theme.schedaBlue
import com.example.gymbuddy.ui.theme.ui.theme.schedaTitle
import com.example.gymbuddy.viewModel.AppViewModelProvider
import com.example.gymbuddy.viewModel.SchedeViewModel
import com.journeyapps.barcodescanner.CameraPreview
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymBuddy(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = NavigationRoute.getRoute(backStackEntry?.destination?.route)

    var themeLight by remember { mutableStateOf(true) }
    var blurDialog by remember { mutableStateOf(0.dp) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .blur(blurDialog),

        topBar = {
            TopAppBarFunction(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                goToProfile = { navController.navigate("Profilo") }
            )
        },
        floatingActionButton = {
            if (currentScreen == "ListaSchede") {
                AddButton(
                    addBlur = { blurDialog = it },
                    onQRCode = { navController.navigate("QRCode Scanner") },
                    onNewScheda = { navController.navigate("Scheda Esercizio/0") },
                )
            } else null
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = if (themeLight) Color.White else Color.DarkGray
            ) {
                NavigationGraph(
                    themeLight,
                    onChangeTheme = { themeLight = !themeLight },
                    navController,
                    innerPadding
                )
            }

        }
    )


}

@Composable
fun ListaSchede(
    navController: NavController? = null,
    viewModel: SchedeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    themeLight: Boolean,
) {
    val schedeUiState by viewModel.schedeUiState.collectAsState()

    if (schedeUiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }
    } else {
        if (schedeUiState.schedeList.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Nessuna scheda presente! \n Utilizza il tasto sottostante per aggiungerne una.",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        } else {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                schedeUiState.schedeList.forEach {
                    SchedaEsercizio(
                        titolo = it.titolo,
                        descrizione = it.descrizione,
                        numeroEsercizi = it.numeroEsercizi.toInt(),
                        ultimoAllenamento = it.ultimoAllenamento,
                        navController = navController,
                        idScheda = it.ID,
                        isFavorite = it.isFavorite,
                        onSwipeToDelete = {
                            viewModel.deleteScheda(it)
                        },
                        toggleFavorite = {
                            viewModel.toggleFavorite(it.ID)
                        }
                    )
                }
            }

        }
    }

}

@Composable
fun TopAppBarFunction(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    goToProfile: () -> Unit = { },
) {
    CenterAlignedTopAppBar(
        title = {
            if (!canNavigateBack || currentScreen == "ListaSchede") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.size(24.dp))
                    Image(
                        painter = painterResource(id = R.drawable.logo_topbar),
                        contentDescription = null,
                        modifier = Modifier.height(50.dp)
                    )
                    if (currentScreen == "ListaSchede") {
                        IconButton(onClick = goToProfile, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "profilo",
                                tint = Color.White
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(24.dp))
                    }

                }
            } else {
                Text(
                    text = currentScreen,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        navigationIcon = {
            //se si può navigare indietro (non home screen) allora appare la freccetta
            if (canNavigateBack && currentScreen != "ListaSchede") {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = "Back button"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = colorResource(id = R.color.main_blue_dark),
        ),
    )
}

@Composable
private fun NavigationGraph(
    themeLight: Boolean,
    onChangeTheme: () -> Unit,
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {


    NavHost(
        navController = navController,
        startDestination = "Login",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("Login") {
            LoginScreen(navController = navController, themeLight = themeLight)
        }
        composable("ListaSchede") {
            ListaSchede(navController = navController, themeLight = themeLight)
        }
        composable(
            "Scheda Esercizio/{schedaId}",
            arguments = listOf(
                navArgument("schedaId") {
                    type = NavType.IntType
                }
            )
        )
        {
            SchedaEsercizi(
                navController = navController,
                idScheda = it.arguments?.getInt("schedaId") ?: 0,
                themeLight = themeLight
            )
        }
        composable("StartAllenamento/{schedaId}", arguments = listOf(navArgument("schedaId") {
            type = NavType.IntType
        }))
        {
            StartAllenamentoScreen(
                navController = navController,
                idScheda = it.arguments?.getInt("schedaId") ?: 0,
                themeLight = themeLight
            )
        }
        composable("QRCode Scanner") {
            CameraScanner(navController = navController, themeLight = themeLight)
        }
        composable("Profilo") {
            ProfiloScreen(
                navController = navController,
                themeLight = themeLight,
                onChangeTheme = onChangeTheme
            )
        }
    }
}


@Composable
fun AddButton(
    addBlur: (Dp) -> Unit = { },
    onQRCode: () -> Unit,
    onNewScheda: () -> Unit,
) {
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        addBlur(10.dp)
        AddDialogChoice(
            showDialog = showDialog.value,
            onDismissRequest = {
                showDialog.value = false
                addBlur(0.dp)
            },
            onQRCode = onQRCode,
            onNewScheda = onNewScheda,
        )
    }
    FloatingActionButton(
        onClick = { showDialog.value = true },
        shape = CircleShape,
        containerColor = colorResource(id = R.color.main_blue_dark),
    )
    {
        Icon(
            Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.White
        )
    }
}


@Composable
fun SchedaEsercizio(
    titolo: String = "Titolo",
    descrizione: String = "Nessuna descrizione",
    numeroEsercizi: Int = 0,
    ultimoAllenamento: String = "Non registrato",
    navController: NavController? = null,
    idScheda: Int,
    isFavorite: Boolean = false,
    onSwipeToDelete: () -> Unit = { },
    toggleFavorite: (Scheda) -> Unit,
) {
    val delete = SwipeAction(
        onSwipe = onSwipeToDelete,
        icon = {
            Row {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.width(24.dp),
                    tint = Color.Black
                )
                Text(
                    text = "Elimina",
                    modifier = Modifier.padding(start = 4.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    style = schedaTitle
                )
            }

        },
        background = Color.Red.copy(alpha = 0.5f),
        isUndo = true,
    )

    SwipeableActionsBox(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        endActions = listOf(delete),
        swipeThreshold = 200.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                )
                .background(color = Color(0xFFF6F6F6))
                .clickable { navController?.navigate("Scheda Esercizio/$idScheda") },
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = titolo,
                        style = schedaTitle
                    )
                    IconButton(
                        onClick = {
                            toggleFavorite(
                                Scheda(
                                    ID = idScheda,
                                    titolo = titolo,
                                    descrizione = descrizione,
                                    ultimoAllenamento = ultimoAllenamento,
                                    numeroEsercizi = numeroEsercizi.toString(),
                                    isFavorite = isFavorite
                                )
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(24.dp)

                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,

                            contentDescription = null,
                            modifier = Modifier.width(24.dp),
                            tint = colorResource(id = R.color.main_blue)
                        )

                    }
                }

                Text(
                    text = descrizione,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                InfoRow(
                    icon = painterResource(id = R.drawable.dumbbell_solid),
                    mainText = "Numero esercizi",
                    value = numeroEsercizi.toString()
                )
                InfoRow(
                    icon = painterResource(id = R.drawable.calendar),
                    mainText = "Ultimo allenamento",
                    value = ultimoAllenamento
                )


            }
        }
    }
}

@Composable
fun InfoRow(icon: Painter, mainText: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start

        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                Modifier.width(24.dp),
                tint = colorResource(id = R.color.main_blue)
            )
            Text(
                text = "$mainText:",
                modifier = Modifier.padding(start = 4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                style = schedaBlue
            )
        }
        Text(
            text = value,
            fontSize = 18.sp,
        )
    }
}

@Composable
fun AddDialogChoice(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onQRCode: () -> Unit = { },
    onNewScheda: () -> Unit = { },
) {

    if (showDialog) {
        AlertDialog(
            modifier = Modifier.shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            ),
            onDismissRequest = onDismissRequest,
            confirmButton = { /*TODO*/ },
            title = {
                Text(
                    text = stringResource(R.string.aggiungi_scheda),
                    style = schedaTitle
                )
            },
            text =
            {
                Column {
                    //NUOVA SCHEDA
                    Spacer(modifier = Modifier.height(8.dp))
                    DialogSelection(
                        icon = painterResource(id = R.drawable.plus_solid),
                        text = stringResource(R.string.nuova_scheda),
                        onClick = {
                            onDismissRequest()
                            onNewScheda()
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    //QR CODE
                    DialogSelection(
                        icon = painterResource(id = R.drawable.qrcode_solid),
                        text = stringResource(R.string.scansiona_qr_code),
                        onClick = {
                            onDismissRequest()
                            onQRCode()
                        }
                    )
                }
            },
            dismissButton = {
                Text(
                    text = stringResource(R.string.dismiss),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Red,
                    modifier = Modifier.clickable(onClick = onDismissRequest),
                )
            },


            )
    }

}

@Composable
fun DialogSelection(icon: Painter, text: String, onClick: () -> Unit = { }) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.clickable(onClick = onClick)

        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                Modifier.width(24.dp),
                tint = colorResource(id = R.color.main_blue)
            )
            Text(
                text = text,
                modifier = Modifier.padding(start = 4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                style = schedaBlue
            )
        }
    }
}



