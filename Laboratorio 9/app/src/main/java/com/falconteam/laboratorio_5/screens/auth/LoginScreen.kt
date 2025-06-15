package com.falconteam.laboratorio_5.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.falconteam.laboratorio_5.ui.theme.Laboratorio5Theme
import com.falconteam.laboratorio_5.ui.theme.Typography

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    onLoginClick()
                }
                is ValidationEvent.Error -> {
                    viewModel.showSnackBar(event.message)
                }
            }
        }
    }

    Laboratorio5Theme {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = viewModel.snackBarHostState.value)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenido!",
                    style = Typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel.username.value,
                    onValueChange = { setUsername ->
                        viewModel.username.value = setUsername
                    },
                    label = { Text("Usuario") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel.password.value,
                    onValueChange = { setPassword ->
                        viewModel.password.value = setPassword
                    },
                    label = { Text("Contraseña") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .width(300.dp),
                    onClick = { viewModel.login() }
                ) {
                    Text(text = "Login")
                }
            }
        }
    }
}