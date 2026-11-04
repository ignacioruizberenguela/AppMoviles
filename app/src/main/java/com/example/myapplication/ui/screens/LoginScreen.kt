package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.myapplication.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,   // recibe route
    onGoToSignup: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    // Roles de prueba (mientras no hay backend)
    val rolesDemo = listOf("Vecino", "Secretaría", "Tesorería", "Directiva", "Admin")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Junta de Vecinos Población Zaror",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("MAIL") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("CONTRASEÑA") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            // error msg
            if (error.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        error = "Ingresa tu mail y contraseña"
                    } else {
                        error = ""
                        onLoginSuccess("Vecino")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("LOGIN")
            }

            TextButton(onClick = { onGoToSignup() }) {
                Text("¿No tienes cuenta? Regístrate")
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // TODO backend, esto es mientras no hay backend. solo UI
            Text(
                text = "Modo demo: entrar como…",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            rolesDemo.forEach { rol ->
                OutlinedButton(
                    onClick = { onLoginSuccess(rol) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(rol)
                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MyApplicationTheme {
        LoginScreen(onLoginSuccess = {}, onGoToSignup = {})
    }
}