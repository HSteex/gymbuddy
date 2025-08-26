package com.example.gymbuddy.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gymbuddy.viewModel.AppViewModelProvider
import com.example.gymbuddy.viewModel.DataStoreManager
import com.example.gymbuddy.viewModel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
    themeLight: Boolean,
) {
    val loginUiState by viewModel.loginUiState.collectAsState()
    val registerUiState by viewModel.registerUiState.collectAsState()
    var preferenceDataStore = DataStoreManager(LocalContext.current)
    if (viewModel.login){
        CoroutineScope(Dispatchers.IO).launch {
            preferenceDataStore.setUsername(loginUiState.username)
        }
        navController.navigate("ListaSchede")
        //navController.popBackStack("Login", true)

        viewModel.login=false
    }


    Column(verticalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.size(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                    )
                    .background(color = Color(0xFFF6F6F6))
            ) {
                Column {
                    if (registerUiState.registerMode) {
                        Text(
                            text = "Registrati!", modifier = Modifier.padding(16.dp)
                        )
                        TextField(
                            value = registerUiState.username,
                            onValueChange = {
                                viewModel.updateRegisterUiState(
                                    username = it,
                                    password = registerUiState.password,
                                    confermaPassword = registerUiState.confermaPassword
                                )
                            },
                            label = { Text("Username") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        TextField(
                            value = registerUiState.password,
                            onValueChange = {
                                viewModel.updateRegisterUiState(
                                    username = registerUiState.username,
                                    password = it,
                                    confermaPassword = registerUiState.confermaPassword
                                )
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            label = { Text("Password") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        TextField(
                            value = registerUiState.confermaPassword,
                            onValueChange = {
                                viewModel.updateRegisterUiState(
                                    username = registerUiState.username,
                                    password = registerUiState.password,
                                    confermaPassword = it
                                )
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            label = { Text("Conferma password") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    } else {
                        Text(
                            text = "Effettua il login!", modifier = Modifier.padding(16.dp)
                        )

                        if (loginUiState.error)
                            Text(
                                text = "Username o password errati",
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp, 0.dp)
                            )

                        TextField(
                            value = loginUiState.username,
                            onValueChange = {
                                viewModel.updateLoginUiState(
                                    username = it, password = loginUiState.password
                                )
                            },
                            label = { Text("Username") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        TextField(
                            value = loginUiState.password,
                            visualTransformation = PasswordVisualTransformation(),
                            onValueChange = {
                                viewModel.updateLoginUiState(
                                    username = loginUiState.username, password = it
                                )
                            },
                            label = { Text("Password") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )

                    }
                }


            }
        }
        Spacer(
            modifier = Modifier.size(16.dp)
        )

        if (!registerUiState.registerMode) {
            Button(
                onClick = {
                    viewModel.login(loginUiState.username, loginUiState.password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp, 0.dp)
                    .shadow(
                        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White
                ),

                ) {
                Text(text = "Login")

            }
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Button(
                onClick = { viewModel.switchRegisterMode() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp, 0.dp)
                    .shadow(
                        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),

                ) {
                Text(text = "Registrati")

            }
        } else {
            Button(
                onClick = {
                    viewModel.register(registerUiState.username, registerUiState.password)
                    if (!registerUiState.error) {
                        CoroutineScope(Dispatchers.IO).launch {
                            preferenceDataStore.setUsername(registerUiState.username)
                        }
                        navController.navigate("ListaSchede")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp, 0.dp)
                    .shadow(
                        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White
                ),

                ) {
                Text(text = "Registrati")

            }
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Button(
                onClick = { viewModel.switchRegisterMode() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp, 0.dp)
                    .shadow(
                        elevation = 10.dp, shape = RoundedCornerShape(8.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),

                ) {
                Text(text = "Torna al login")

            }
        }
    }
}