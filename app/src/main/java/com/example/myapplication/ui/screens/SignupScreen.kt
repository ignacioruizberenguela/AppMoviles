package com.example.myapplication.ui.screens

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
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodel.AuthViewModel

@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    onSignupSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val estado by authViewModel.registroState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear Cuenta Nueva",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Registro para vecinos de la Población Zaror",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = { authViewModel.onNombreChange(it) },
            label = { Text("NOMBRE") },
            singleLine = true,
            isError = estado.errorNombre != null,
            supportingText = { estado.errorNombre?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.rut,
            onValueChange = { authViewModel.onRutChange(it) },
            label = { Text("RUT") },
            placeholder = { Text("12.345.678-5") },
            singleLine = true,
            isError = estado.errorRut != null,
            supportingText = { estado.errorRut?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.email,
            onValueChange = { authViewModel.onRegistroEmailChange(it) },
            label = { Text("MAIL") },
            singleLine = true,
            isError = estado.errorEmail != null,
            supportingText = { estado.errorEmail?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.password,
            onValueChange = { authViewModel.onRegistroPasswordChange(it) },
            label = { Text("CONTRASEÑA") },
            singleLine = true,
            isError = estado.errorPassword != null,
            supportingText = { estado.errorPassword?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        estado.errorGeneral?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (authViewModel.registrar()) onSignupSuccess()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "REGISTRARSE")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = { onBackToLogin() }) {
            Text(text = "¿Ya tienes una cuenta? Inicia sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupPreview() {
    MyApplicationTheme {
        SignupScreen(authViewModel = viewModel(), onSignupSuccess = {}, onBackToLogin = {})
    }
}