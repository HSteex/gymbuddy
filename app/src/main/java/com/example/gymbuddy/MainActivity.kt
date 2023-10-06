@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gymbuddy

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import android.media.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxHeight

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.gymbuddy.ui.theme.GymBuddyTheme
import com.example.gymbuddy.ui.theme.ui.theme.schedaBlue
import com.example.gymbuddy.ui.theme.ui.theme.schedaTitle


class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymBuddyTheme {
                // A surface container using the 'background' color from the theme
                //Create a scaffold with a top bar and a floating action button
                var blurDialog by remember { mutableStateOf(0.dp) }
                Scaffold (
                    modifier= Modifier
                        .fillMaxSize()
                        .blur(blurDialog),

                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                    androidx.compose.foundation.Image(
                                        painter = painterResource(id = R.drawable.logo_topbar),
                                        contentDescription = null,
                                        modifier= Modifier.height(50.dp)
                                    )
                            },
                                colors = TopAppBarDefaults.mediumTopAppBarColors(
                                    containerColor = colorResource(id = R.color.main_blue_dark),
                                ),
                            )
                    },
                    floatingActionButton = {
                        AddButton(
                            addBlur = { blurDialog = it }
                        )
                    },
                    floatingActionButtonPosition = FabPosition.Center,
                    content = { innerPadding ->
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding), color = MaterialTheme.colorScheme.background)
                        {
                            Column (modifier=Modifier.verticalScroll(rememberScrollState())) {
                                SchedaEsercizio(titolo = "Titolo 1", descrizione = "Allenamento per spalle e petto, distruzione assicurata poi scrivo cose a caso per testare sto robo e vediamo se taglia bene qujando arriva a 3 righe perchè ho messo cosi e mi iace", numeroEsercizi = 10)
                                SchedaEsercizio(titolo = "Titolo 2", descrizione = "Allenamento per spalle e petto, distruzione assicurata poi scrivo cose a caso per testare sto robo e vediamo se taglia bene qujando arriva a 3 righe perchè ho messo cosi e mi iace", numeroEsercizi = 10)
                                SchedaEsercizio(titolo = "Titolo 1", descrizione = "Allenamento per spalle e petto, distruzione assicurata poi scrivo cose a caso per testare sto robo e vediamo se taglia bene qujando arriva a 3 righe perchè ho messo cosi e mi iace", numeroEsercizi = 10)
                                SchedaEsercizio(titolo = "Titolo 1", descrizione = "Allenamento per spalle e petto, distruzione assicurata poi scrivo cose a caso per testare sto robo e vediamo se taglia bene qujando arriva a 3 righe perchè ho messo cosi e mi iace", numeroEsercizi = 10)

                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AddButton(
    addBlur : (Dp) -> Unit = { },
)
{
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        addBlur(10.dp)
        AddDialogChoice(
            showDialog = showDialog.value,
            onDismissRequest = {
                showDialog.value = false
                addBlur(0.dp)})
    }
    FloatingActionButton(
        onClick = {showDialog.value = true},
        shape= CircleShape,
        )
        {
        Icon(Icons.Default.Add, contentDescription = "Add")
    }
}

@Composable
fun SchedaEsercizio(titolo: String = "Titolo", descrizione: String ="Nessuna descrizione", numeroEsercizi : Int =0, ultimoAllenamento : String="Non registrato"){
    Box(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()){
        Box(modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = Color(0xFFF6F6F6))
            ){
            Column (modifier=Modifier.padding(16.dp)){

                Text(
                    text = titolo,
                    style = schedaTitle)
                Text(text = descrizione,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                    )

                InfoRow(icon = painterResource(id = R.drawable.dumbbell_solid), mainText = "Numero esercizi" , value = numeroEsercizi.toString() )
                InfoRow(icon = painterResource(id = R.drawable.calendar), mainText = "Ultimo allenamento", value = "22/05/2023")


            }
        }
    }
}

@Composable
fun InfoRow(icon : Painter, mainText: String, value : String){
    Row (horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start

        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                Modifier.width(24.dp),
                tint = colorResource(id = R.color.main_blue))
            Text(
                text="$mainText:",
                modifier=Modifier.padding(start = 4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                style = schedaBlue
            )
        }
        Text(text = value,
            fontSize = 18.sp,
        )
    }
}

@Composable
fun AddDialogChoice(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,

) {
    if (showDialog) {
        AlertDialog(
            modifier=Modifier.shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp)
            ),
            onDismissRequest = onDismissRequest,
            confirmButton = { /*TODO*/ },
            title = {
                Text(text = stringResource(R.string.aggiungi_scheda),
                    style= schedaTitle)
            },
            text =
            {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    DialogSelection(
                        icon = painterResource(id = R.drawable.plus_solid),
                        text = stringResource(R.string.nuova_scheda)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    DialogSelection(
                        icon = painterResource(id = R.drawable.qrcode_solid),
                        text = stringResource(R.string.scansiona_qr_code)
                    )
                }
            },
            dismissButton = {
                Text(
                    text = stringResource(R.string.dismiss),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Red,
                    modifier=Modifier.clickable(onClick = onDismissRequest),
                )
            },


            )
    }

}

@Composable
fun DialogSelection(icon : Painter, text : String, onClick : () -> Unit = { }){
    Row (horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier=Modifier.clickable(onClick = onClick)

        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                Modifier.width(24.dp),
                tint = colorResource(id = R.color.main_blue))
            Text(
                text=text,
                modifier=Modifier.padding(start = 4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                style = schedaBlue
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GymBuddyTheme {
       SchedaEsercizio(titolo = "Titolo 2", descrizione = "Allenamento per spalle e petto, distruzione assicurata", numeroEsercizi = 10)
    }
}

