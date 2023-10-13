package com.example.gymbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.gymbuddy.ui.theme.ButtonGrey
import com.example.gymbuddy.ui.theme.MainBlue
import com.example.gymbuddy.ui.theme.PlayGreen
import com.example.gymbuddy.ui.theme.ui.theme.GymBuddyTheme
import com.example.gymbuddy.ui.theme.ui.theme.schedaBlue
import com.example.gymbuddy.ui.theme.ui.theme.schedaTitle


@Composable
fun SchedaEsercizi(navController: NavHostController ){
    Column {
        InfoScheda(
            "Titolo",
            "Descrizione lunga in cui scrivo cose incredibili, sta volta può essere piu lunga di prima, ma non troppo, altrimenti non va bene e non si vede bene, quindi non va bene"
        )

        LastTraining(data = "12/12/2021")
        TextEsercizi()
    }
}

@Composable
fun InfoScheda(titolo: String, descrizione: String) {
    Box(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(color = Color(0xFFF6F6F6))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = titolo, style= schedaTitle)
                    IconButton(
                        onClick= { /*TODO*/ },
                        modifier= Modifier
                            .size(52.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape),
                        colors=IconButtonDefaults.iconButtonColors(
                            containerColor= ButtonGrey,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Avvia scheda",
                            tint = PlayGreen,
                            modifier =Modifier.size(50.dp),
                            )
                    }
                }
                Text(text = descrizione)
                Spacer(modifier = Modifier.size(16.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick= { /*TODO*/ },
                        modifier= Modifier
                            .size(48.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape),
                        colors=IconButtonDefaults.iconButtonColors(
                            containerColor= ButtonGrey,
                        ),
                        ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Condividi scheda",
                            modifier = Modifier.size(24.dp),
                            tint= MainBlue

                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    IconButton(
                        onClick= { /*TODO*/ },
                        modifier= Modifier
                            .size(48.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape),
                        colors=IconButtonDefaults.iconButtonColors(
                            containerColor= ButtonGrey,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Modifica scheda",
                            modifier = Modifier.size(24.dp),
                            tint= MainBlue

                        )
                    }

                }

            }
        }
    }

}

@Composable
fun LastTraining(data: String){
    Box(modifier = Modifier
        .padding( 16.dp, 0.dp, 16.dp, 16.dp)
        .fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(8.dp)
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
fun TextEsercizi(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Esercizi", style= schedaBlue)
            Spacer(modifier = Modifier.size(8.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .height(2.dp)
                .background(color = MainBlue))
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

