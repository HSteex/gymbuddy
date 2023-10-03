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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.gymbuddy.ui.theme.GymBuddyTheme


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymBuddyTheme {
                // A surface container using the 'background' color from the theme
                //Create a scaffold with a top bar and a floating action button
                Scaffold (
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
                        AddButton()
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
fun AddButton(){
    FloatingActionButton(
        onClick = { /*TODO*/},
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
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
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
                style = TextStyle(color= colorResource(id = R.color.main_blue))
            )
        }
        Text(text = value,
            fontSize = 18.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GymBuddyTheme {
       SchedaEsercizio(titolo = "Titolo 2", descrizione = "Allenamento per spalle e petto, distruzione assicurata", numeroEsercizi = 10)
    }
}

