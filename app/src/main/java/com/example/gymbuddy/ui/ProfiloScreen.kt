package com.example.gymbuddy.ui

import android.location.Geocoder
import android.location.Location
import android.provider.Settings
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gymbuddy.R
import com.example.gymbuddy.ui.theme.ui.theme.MainBlue
import com.example.gymbuddy.ui.theme.ui.theme.schedaBlue
import com.example.gymbuddy.ui.theme.ui.theme.schedaTitle
import java.util.Locale

@Composable
fun ProfiloScreen(
    navController: NavController,
    themeLight: Boolean,
    onChangeTheme: () -> Unit,

    ) {
    var _location by remember { mutableStateOf(Location("")) }
    val context = LocalContext.current
    val geocoder = Geocoder(context, Locale.getDefault())
    var isFounded by remember { mutableStateOf(false) }
    var switchChecked by remember { mutableStateOf(!themeLight) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(
                        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                    )
                    .background(color = Color(0xFFF6F6F6))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .shadow(
                                elevation = 10.dp, shape = RoundedCornerShape(50.dp)
                            )
                            .background(color = Color(0xFFF6F6F6)),
                        painter = painterResource(id = R.drawable.ste),
                        contentDescription = "propic",
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                    Text(text = "Stefano", modifier = Modifier.padding(8.dp))

                }

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(
                        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                    )
                    .background(color = Color(0xFFF6F6F6))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text(text = "Palestra preferita: ", style = schedaBlue)
                        LocationButton(onLocationReceived = { location ->

                            _location = location
                            isFounded = true

                        })

                    }
                    @Suppress("DEPRECATION")
                    if (isFounded) {
                        val addresses =
                            geocoder.getFromLocation(_location.latitude, _location.longitude, 1)
                        val address = addresses?.get(0)?.getAddressLine(0) ?: ""
                        Text(
                            text = address, style = schedaTitle
                        )
                    }
                }

            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(
                        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                    )
                    .background(color = Color(0xFFF6F6F6))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Impostazioni", style = schedaBlue)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.circle_half_stroke_solid),
                                contentDescription = "tema",
                                modifier = Modifier.size(24.dp)
                            )
                            Text(text = "Tema chiaro/scuro:", style = schedaTitle)
                        }

                        Switch(
                            checked = switchChecked,
                            onCheckedChange = {
                                switchChecked = !switchChecked
                                onChangeTheme()
                            },
                            modifier = Modifier.size(24.dp),
                            colors = androidx.compose.material3.SwitchDefaults.colors(
                                checkedThumbColor = Color.White, checkedTrackColor = MainBlue
                            )
                        )
                    }


                }
            }

        }
    }
}