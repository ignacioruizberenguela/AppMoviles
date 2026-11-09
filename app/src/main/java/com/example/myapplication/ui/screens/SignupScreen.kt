package com.example.myapplication.ui.screens



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    onSignupSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Crear Cuenta Nueva",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Registro para vecinos y directiva",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("NOMBRE") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo de Mail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("MAIL") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("CONTRASEÑA") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo de RUT (específico del wireframe de signup)
            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("RUT") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // mensaje error
            if (error.isNotEmpty()) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botón de Registro
            Button(
                onClick = { onSignupSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "REGISTRARSE")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón para volver al Login si ya tiene cuenta
            TextButton(
                onClick = {
                    val resultado = authViewModel.registrar(nombre, rut, email, password)
                    if (resultado == null) onSignupSuccess() else error = resultado
                },
            ) {
                Text(text = "¿Ya tienes una cuenta? Inicia sesión")
            }

        }

    }

@Preview(showBackground = true)
@Composable
fun RegistroPreview() {
    MyApplicationTheme {
        SignupScreen(authViewModel = viewModel(), onSignupSuccess = {}, onBackToLogin = {})
    }
}
