package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.model.Rol
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: (Rol) -> Unit,
    onGoToSignup: () -> Unit
) {
    // Observa el StateFlow: cada cambio redibuja la pantalla
    val estado by authViewModel.loginState.collectAsState()

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
            value = estado.email,
            onValueChange = { authViewModel.onLoginEmailChange(it) },
            label = { Text("MAIL") },
            singleLine = true,
            isError = estado.errorEmail != null,
            supportingText = { estado.errorEmail?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.password,
            onValueChange = { authViewModel.onLoginPasswordChange(it) },
            label = { Text("CONTRASEÑA") },
            singleLine = true,
            isError = estado.errorPassword != null,
            supportingText = { estado.errorPassword?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        // Error general (mail o contraseña incorrectos)
        // Aparece y desaparece con animación
        AnimatedVisibility(visible = estado.errorGeneral != null) {
            Text(
                text = estado.errorGeneral ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (authViewModel.iniciarSesion()) {
                    authViewModel.usuarioActual?.let { onLoginSuccess(it.rol) }
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

        // Modo demo (mientras no hay backend)
        Text(
            text = "Modo demo: entrar como…",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "(o usa por ejemplo maria@correo.cl / 1234)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        Rol.entries.forEach { rol ->
            OutlinedButton(
                onClick = {
                    authViewModel.entrarComoDemo(rol)
                    onLoginSuccess(rol)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(rol.nombre)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MyApplicationTheme {
        LoginScreen(authViewModel = viewModel(), onLoginSuccess = {}, onGoToSignup = {})
    }
}