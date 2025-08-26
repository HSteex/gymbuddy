package com.example.gymbuddy.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymbuddy.R
import com.example.gymbuddy.data.Esercizi
import com.example.gymbuddy.ui.theme.ButtonGrey
import com.example.gymbuddy.ui.theme.MainBlue
import com.example.gymbuddy.ui.theme.PauseOrange
import com.example.gymbuddy.ui.theme.ui.theme.schedaBlue
import com.example.gymbuddy.ui.theme.ui.theme.schedaTitle
import com.example.gymbuddy.viewModel.AppViewModelProvider
import com.example.gymbuddy.viewModel.StartAllenamentoViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun StartAllenamentoScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: StartAllenamentoViewModel = viewModel(factory = AppViewModelProvider.Factory),
    idScheda: Int,
    themeLight: Boolean,
) {
    val startAllenamentoUiState by viewModel.startAllenamentoUiState.collectAsState()

    val timerColor = if (startAllenamentoUiState.isPaused) PauseOrange else Color.Green
    val coroutineScope = rememberCoroutineScope()
    if (startAllenamentoUiState.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = Color.Blue,
            )
        }
    } else {
        Column {
            if (startAllenamentoUiState.pausa) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Pausa",
                        textAlign = TextAlign.Center,
                        style = schedaTitle,
                        modifier = Modifier.padding(16.dp)
                    )
                    CircularTimer(
                        durata = startAllenamentoUiState.durata,
                        timeLeft = startAllenamentoUiState.timeLeft,
                        circulaColor = timerColor,
                        ripetizione = 0,
                    )
                    ButtonsRow(

                        onPlayPause = {
                            viewModel.onPlayPause()
                        },
                        onSkip = {
                            viewModel.onSkip()
                        },
                        isPaused = startAllenamentoUiState.isPaused,
                        timeLeft = startAllenamentoUiState.timeLeft
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    NextExercise(
                        startAllenamentoUiState.prossimoEsercizio,
                        onClick = {
                            coroutineScope.launch { viewModel.onEnd() }
                            navController.popBackStack()
                        }
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = startAllenamentoUiState.nomeEsercizio,
                        textAlign = TextAlign.Center,
                        style = schedaTitle,
                        modifier = Modifier.padding(16.dp)
                    )
                    CircularTimer(
                        durata = 1,
                        timeLeft = 0,
                        circulaColor = MainBlue,
                        ripetizione = startAllenamentoUiState.ripetizioni,
                    )
                    ButtonsRow(

                        onPlayPause = {
                            viewModel.onPlayPause()
                        },
                        onSkip = {
                            viewModel.onSkip()
                        },
                        isPaused = startAllenamentoUiState.isPaused,
                        timeLeft = startAllenamentoUiState.timeLeft
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    NextExercise(
                        startAllenamentoUiState.prossimoEsercizio,
                        onClick = {
                            coroutineScope.launch { viewModel.onEnd() }
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CircularTimer(
    durata: Int,
    modifier: Modifier = Modifier,
    timeLeft: Int,
    circulaColor: Color,
    ripetizione: Int,
) {
    val boxSize = LocalConfiguration.current.screenWidthDp.dp
    Box(
        modifier = Modifier
            .padding(16.dp)
            .height(boxSize)
            .width(boxSize)
    ) {
        CircularProgressIndicator(
            progress = 1f,
            color = Color.Gray,
            modifier = Modifier
                .size(
                    boxSize
                )
                .padding(1.dp),
            strokeWidth = 6.dp
        )
        CircularProgressIndicator(
            progress = 1 - timeLeft.toFloat() / durata.toFloat(),
            color = circulaColor,
            modifier = Modifier.size(
                boxSize
            ),
            strokeWidth = 8.dp
        )
        Column(
            modifier = Modifier
                .height(boxSize)
                .width(boxSize)
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (timeLeft > 0) {
                Text(
                    "Tempo rimanente:",
                    textAlign = TextAlign.Center,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Text(
                    text = String.format("%02d:%02d", (timeLeft / 60), timeLeft % 60),
                    textAlign = TextAlign.Center,
                    style = schedaBlue
                )
            } else {
                if (ripetizione > 0) {
                    Text(
                        text = "Ripetizioni: $ripetizione",
                        textAlign = TextAlign.Center,
                        style = schedaBlue
                    )

                } else {
                    Text(
                        text = "Pausa terminata!",
                        textAlign = TextAlign.Center,
                        style = schedaBlue
                    )
                }
            }
        }

    }

}

@Composable
fun ButtonsRow(
    onPlayPause: () -> Unit,
    onSkip: () -> Unit,
    isPaused: Boolean,
    timeLeft: Int,
) {
    val playOrPauseIcon =
        if (isPaused) painterResource(id = R.drawable.play_solid) else painterResource(id = R.drawable.pause_solid) //FIXME
    val colorIcon = if (isPaused) Color.Green else PauseOrange
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (timeLeft > 0) {
            IconButton(
                onClick = { onPlayPause() },
                modifier = Modifier
                    .width(96.dp)
                    .size(48.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = ButtonGrey,
                ),
            ) {
                Icon(
                    painter = playOrPauseIcon as Painter,
                    contentDescription = "Play/Pausa",
                    modifier = Modifier.size(24.dp),
                    tint = colorIcon

                )
            }
            Spacer(modifier = Modifier.size(16.dp))
        }
        IconButton(
            onClick = { onSkip() },
            modifier = Modifier
                .width(96.dp)
                .size(48.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = ButtonGrey,
            ),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.forward_solid),
                contentDescription = "Modifica scheda",
                modifier = Modifier.size(24.dp),
                tint = MainBlue

            )
        }

    }
}

@Composable
fun NextExercise(
    prossimoEsercizio: Esercizi?,
    onClick: () -> Unit = { }
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (prossimoEsercizio != null) {
            Text(text = "Prossimo esercizio:", style = schedaTitle)
            Spacer(modifier = Modifier.size(16.dp))
            Esercizio(esercizio = prossimoEsercizio!!)
        } else {
            TerminaAllenamentoButton(onClick = onClick)
        }
    }
}

@Composable
fun TerminaAllenamentoButton(
    onClick: () -> Unit,
) {
    Button(
        onClick =  onClick ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MainBlue,
            contentColor = Color.White
        ),
        shape = CircleShape
    ) {
        Text(text = "Termina allenamento")
    }
}

@Preview
@Composable
fun PreviewStartAllenamentoScreen() {
}
