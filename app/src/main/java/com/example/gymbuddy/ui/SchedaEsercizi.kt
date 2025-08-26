package com.example.gymbuddy.ui

import android.graphics.Bitmap
import android.util.DisplayMetrics
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gymbuddy.R
import com.example.gymbuddy.data.Esercizi
import com.example.gymbuddy.data.Scheda
import com.example.gymbuddy.ui.theme.ButtonGrey
import com.example.gymbuddy.ui.theme.MainBlue
import com.example.gymbuddy.ui.theme.PlayGreen
import com.example.gymbuddy.ui.theme.ui.theme.GymBuddyTheme
import com.example.gymbuddy.ui.theme.ui.theme.schedaBlue
import com.example.gymbuddy.ui.theme.ui.theme.schedaTitle
import com.example.gymbuddy.viewModel.AppViewModelProvider
import com.example.gymbuddy.viewModel.SchedaDetailViewModel
import com.example.gymbuddy.viewModel.SchedeViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.encoder.QRCode
import com.journeyapps.barcodescanner.BarcodeEncoder


@Composable
fun SchedaEsercizi(
    navController: NavHostController,
    viewModel: SchedaDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    idScheda: Int,
    themeLight: Boolean,

    ) {
    //Variabili per la scheda
    val schedaDetailUiState by viewModel.schedaDetailUiState.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    var json = remember { mutableStateOf("") }

    if (viewModel.editMode) {
        EditMode(
            navController = navController, viewModel = viewModel, idScheda = idScheda
        )
    } else {

        if (schedaDetailUiState.isLoading == false) {
            if (showDialog.value) {
                QRCodeDialog(showDialog = showDialog.value,
                    json = json.value,
                    onDismiss = { showDialog.value = false })
            }
            Column {
                InfoScheda(schedaDetailUiState.scheda.titolo,
                    schedaDetailUiState.scheda.descrizione,
                    onPlayPress = {
                        navController.navigate("startAllenamento/$idScheda")
                    },
                    enterEditMode = {
                        viewModel.enterEditMode()
                    },
                    onShare = {
                        json.value = viewModel.schedaToJson()
                        showDialog.value = true
                    })
                LastTraining(data = schedaDetailUiState.scheda.ultimoAllenamento)
                TextEsercizi()
                if (schedaDetailUiState.esercizi.isNotEmpty()) {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        schedaDetailUiState.esercizi.forEach { esercizio ->
                            Esercizio(esercizio)
                        }
                    }
                }
            }
        }
    }


}

@Composable
fun QRCodeDialog(
    showDialog: Boolean, json: String, onDismiss: () -> Unit = {}
) {
    if (showDialog) {
        AlertDialog(
            modifier = Modifier.shadow(
                elevation = 10.dp, shape = RoundedCornerShape(8.dp)
            ),
            onDismissRequest = onDismiss,
            confirmButton = { /*TODO*/ },
            title = {
                Text(
                    text = "Condividi il QR Code", style = schedaTitle
                )
            },
            text = {
                val bitmatrix: BitMatrix =
                    MultiFormatWriter().encode(json, BarcodeFormat.QR_CODE, 1000, 1000)
                val barcodeEncoder: BarcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.createBitmap(bitmatrix)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                        .background(color = Color.Red)

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Green)
                    ) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "QRCode",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            dismissButton = {
                Text(
                    text = "Chiudi",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Red,
                    modifier = Modifier.clickable(onClick = onDismiss),
                )
            },
        )
    }
}

@Composable
fun Esercizio(
    esercizio: Esercizi
) {
    Box(
        modifier = Modifier
            .padding(16.dp, 0.dp, 16.dp, 16.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                )
                .background(color = Color(0xFFF6F6F6))
                .padding(8.dp)
        ) {
            if (esercizio.pausa) {
                InfoRow(
                    icon = painterResource(id = R.drawable.clock_solid),
                    mainText = "Recupero",
                    value = "${esercizio.durata} secondi"
                )
            } else {
                InfoRow(
                    icon = painterResource(id = R.drawable.dumbbell_solid),
                    mainText = esercizio.nome,
                    value = "${esercizio.ripetizioni} ripetizioni"
                )
            }
        }
    }
}

@Composable
fun InfoScheda(
    titolo: String,
    descrizione: String,
    onPlayPress: () -> Unit = {},
    enterEditMode: () -> Unit = {},
    onShare: () -> Unit = {}
) {
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = titolo, style = schedaTitle)
                    IconButton(
                        onClick = { onPlayPress() },
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = ButtonGrey,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Avvia scheda",
                            tint = PlayGreen,
                            modifier = Modifier.size(50.dp),
                        )
                    }
                }
                Text(text = descrizione)
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onShare() },
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = ButtonGrey,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Condividi scheda",
                            modifier = Modifier.size(24.dp),
                            tint = MainBlue

                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    IconButton(
                        onClick = { enterEditMode() },
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = ButtonGrey,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Modifica scheda",
                            modifier = Modifier.size(24.dp),
                            tint = MainBlue

                        )
                    }

                }

            }
        }
    }

}

@Composable
fun LastTraining(data: String) {
    Box(
        modifier = Modifier
            .padding(16.dp, 0.dp, 16.dp, 16.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                )
                .background(color = Color(0xFFF6F6F6))
                .padding(16.dp)
        ) {
            InfoRow(
                icon = painterResource(id = R.drawable.calendar),
                mainText = "Ultimo allenamento",
                value = data
            )
        }
    }
}

@Composable
fun TextEsercizi() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Esercizi", style = schedaBlue)
            Spacer(modifier = Modifier.size(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .height(2.dp)
                    .background(color = MainBlue)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GymBuddyTheme {
        Column {
            InfoScheda(
                "Titolo",
                "Descrizione lunga in cui scrivo cose incredibili, sta volta può essere piu lunga di prima, ma non troppo, altrimenti non va bene e non si vede bene, quindi non va bene"
            )

            LastTraining(data = "12/12/2021")
            TextEsercizi()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMode(
    navController: NavHostController, viewModel: SchedaDetailViewModel, idScheda: Int
) {
    val modifiedSchedaDetailUiState by viewModel.modifiedSchedaDetailUiState.collectAsState()

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
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
                    Text(text = "Titolo")
                    TextField(value = modifiedSchedaDetailUiState.scheda.titolo,
                        onValueChange = { viewModel.updateTitle(it) })
                    Text(text = "Descrizione")
                    TextField(value = modifiedSchedaDetailUiState.scheda.descrizione,
                        onValueChange = { viewModel.updateDesc(it) })
                }

            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        TextEsercizi()

            Column {
                if (modifiedSchedaDetailUiState.esercizi.isNotEmpty()) {
                modifiedSchedaDetailUiState.esercizi.forEach { esercizio ->
                    EsercizioEdit(esercizio,
                        eserciziEnum = viewModel.eserciziEnum,
                        changeDurataPausa = { durata, esercizio ->
                            viewModel.changeDurataPausa(
                                durata, esercizio
                            )
                        },
                        changeNomeEsercizio = { nome, esercizio ->
                            viewModel.changeNomeEsercizio(
                                nome, esercizio
                            )
                        },
                        changeRipetizioni = { ripetizioni, esercizio ->
                            viewModel.changeRipetizioni(
                                ripetizioni, esercizio
                            )
                        },
                        deleteEsercizio = { esercizio ->
                            viewModel.deleteEsercizio(
                                esercizio
                            )
                        }
                    )
                }
                }
                NewEsercizioButton(addEsercizio = { viewModel.addEsercizio() },
                    addPausa = { viewModel.addPausa() })
                Spacer(modifier = Modifier.size(50.dp))
        }
    }
    SaveOrDiscardButtons(onSave = {
        viewModel.saveScheda()
        viewModel.exitEditMode()
    }, onDiscard = {
        if(!viewModel.exitEditMode()){
            navController.popBackStack()
        }
    })

}

@Composable
fun NewEsercizioButton(
    addEsercizio: () -> Unit = {}, addPausa: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 16.dp, 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { addEsercizio() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = ButtonGrey,
            ),
        ) {
            Row() {
                Text(
                    text = "Esercizio", style = schedaBlue
                )
                Icon(
                    painter = painterResource(id = R.drawable.plus_solid),
                    contentDescription = "Aggiungi esercizio",
                    modifier = Modifier.size(24.dp),
                    tint = MainBlue

                )
            }

        }
        Spacer(modifier = Modifier.size(16.dp))
        IconButton(
            onClick = { addPausa() },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = ButtonGrey,
            ),
        ) {
            Row() {
                Text(
                    text = "Pausa", style = schedaBlue
                )
                Icon(
                    painter = painterResource(id = R.drawable.plus_solid),
                    contentDescription = "Aggiungi pausa",
                    modifier = Modifier.size(24.dp),
                    tint = MainBlue

                )
            }
        }
    }
}

@Composable
fun SaveOrDiscardButtons(
    onSave: () -> Unit = {}, onDiscard: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .align(Alignment.BottomCenter) // Allinea la Row in basso al centro
        ) {
            Button(
                onClick = { onDiscard() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                shape = RectangleShape,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .shadow(10.dp, RectangleShape)


            ) {
                Text(
                    "Annulla", color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(color = Color.Black)
            )

            Button(
                onClick = { onSave() }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green
                ), shape = RectangleShape, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    "Salva", color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EsercizioEdit(
    esercizio: Esercizi,
    eserciziEnum: List<String>,
    changeDurataPausa: (String, Esercizi) -> Unit,
    changeNomeEsercizio: (String, Esercizi) -> Unit,
    changeRipetizioni: (String, Esercizi) -> Unit,
    deleteEsercizio: (Esercizi) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember {mutableStateOf(0)}
    if(!esercizio.pausa){
        selectedIndex=eserciziEnum.indexOf(esercizio.nome)
    }
    Box(
        modifier = Modifier
            .padding(16.dp, 0.dp, 16.dp, 16.dp)
            .fillMaxWidth()
    ) {
        Row (
            modifier=Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .shadow(
                        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                    )
                    .background(color = Color(0xFFF6F6F6))
                    .padding(8.dp)
            ) {
                if (esercizio.pausa) {
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
                                painter = painterResource(id = R.drawable.clock_solid),
                                contentDescription = null,
                                Modifier.width(24.dp),
                                tint = colorResource(id = R.color.main_blue)
                            )
                            Text(
                                text = "Recupero",
                                modifier = Modifier.padding(start = 4.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                style = schedaBlue
                            )
                        }
                        TextField(
                            value = esercizio.durata.toString(),
                            onValueChange = { changeDurataPausa(it, esercizio) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp),
                            label = { Text("secondi") },
                        )
                    }

                } else {
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
                                painter = painterResource(id = R.drawable.dumbbell_solid),
                                contentDescription = null,
                                Modifier.width(24.dp),
                                tint = colorResource(id = R.color.main_blue)
                            )
                            Row(modifier = Modifier.clickable { expanded = true }) {
                                Text(
                                    text = eserciziEnum[selectedIndex],
                                    modifier = Modifier.padding(start = 4.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    style = schedaBlue
                                )

                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = MainBlue

                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(300.dp)
                                ) {
                                    eserciziEnum.forEachIndexed { index, option ->
                                        DropdownMenuItem(onClick = {
                                            selectedIndex = index
                                            expanded = false
                                            changeNomeEsercizio(option, esercizio)
                                        }, text = {
                                            Text(
                                                text = option,
                                                color = MainBlue,
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        })
                                    }
                                }
                            }

                        }
                        TextField(
                            value = esercizio.ripetizioni.toString(),
                            onValueChange = { changeRipetizioni(it, esercizio) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp),
                            label = { Text("ripetizioni") },
                        )
                    }
                    //                InfoRow(
                    //                    icon = painterResource(id = R.drawable.dumbbell_solid),
                    //                    mainText = esercizio.nome,
                    //                    value = "${esercizio.ripetizioni} ripetizioni"
                    //                )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            DeleteButton(delete = {
                selectedIndex = 0
                deleteEsercizio(esercizio)})
        }
    }
}

@Composable
fun DeleteButton(
    delete: () -> Unit = {}
){
    IconButton(
        onClick = { delete() },
        modifier = Modifier
            .size(48.dp)
            .shadow(4.dp, CircleShape)
            .clip(CircleShape),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = ButtonGrey,
        ),
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Rimuovi esercizio",
            modifier = Modifier.size(24.dp),
            tint = Color.Red

        )
    }
}



