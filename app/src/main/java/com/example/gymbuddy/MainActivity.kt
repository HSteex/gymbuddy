@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gymbuddy


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

                GymBuddy()


            }
        }
    }
}

@Composable
fun GymBuddy(
    navController: NavHostController=rememberNavController()
){
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: "GymBuddy"

    var blurDialog by remember { mutableStateOf(0.dp) }

    Scaffold (
        modifier= Modifier
            .fillMaxSize()
            .blur(blurDialog),

        topBar = {
            TopAppBarFunction(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
            )
        },
        floatingActionButton = {
            AddButton(
                addBlur = { blurDialog = it }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerPadding ->
//            Surface(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding), color = MaterialTheme.colorScheme.background)
//            {
//                NavigationGraph(navController, innerPadding)
//            }
            NavigationGraph(navController, innerPadding)
        }
    )



}

@Composable
fun TopAppBarFunction(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
)
{
    CenterAlignedTopAppBar(
        title = {
            if (!canNavigateBack) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.logo_topbar),
                    contentDescription = null,
                    modifier = Modifier.height(50.dp)
                )
            }
            else{
                Text(
                    text = currentScreen,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        navigationIcon = {
            //se si può navigare indietro (non home screen) allora appare la freccetta
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint=Color.White,
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
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "ListaSchede",
        modifier= Modifier.padding(innerPadding)
    ) {
        composable("ListaSchede") {
            ListaSchede(navController = navController)
        }
        composable("Scheda Esercizio") {
            SchedaEsercizi( navController = navController)
        }
    }
}

@Composable
fun ListaSchede(navController: NavController? = null, modifier: Modifier = Modifier){
    Column (modifier=Modifier.verticalScroll(rememberScrollState())) {
        SchedaEsercizio(titolo = "Titolo 1", descrizione = "Allenamento per spalle e petto, distruzione assicurata poi scrivo cose a caso per testare sto robo e vediamo se taglia bene qujando arriva a 3 righe perchè ho messo cosi e mi iace", numeroEsercizi = 10, navController=navController)
        SchedaEsercizio(titolo = "Titolo 2", descrizione = "Allenamento per spalle e petto, distruzione assicurata poi scrivo cose a caso per testare sto robo e vediamo se taglia bene qujando arriva a 3 righe perchè ho messo cosi e mi iace", numeroEsercizi = 10, navController=navController)
        SchedaEsercizio(titolo = "Titolo 1", descrizione = "Allenamento per spalle e petto, distruzione assicurata poi scrivo cose a caso per testare sto robo e vediamo se taglia bene qujando arriva a 3 righe perchè ho messo cosi e mi iace", numeroEsercizi = 10, navController=navController)
        SchedaEsercizio(titolo = "Titolo 1", descrizione = "Allenamento per spalle e petto, distruzione assicurata poi scrivo cose a caso per testare sto robo e vediamo se taglia bene qujando arriva a 3 righe perchè ho messo cosi e mi iace", numeroEsercizi = 10, navController=navController)

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
fun SchedaEsercizio(titolo: String = "Titolo", descrizione: String ="Nessuna descrizione", numeroEsercizi : Int =0, ultimoAllenamento : String="Non registrato",navController : NavController? = null){
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
            .clickable { navController?.navigate("Scheda Esercizio") }
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

