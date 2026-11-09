package com.example.myapplication.ui.screens



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TesoreriaScreen(
    onNavigate: (String) -> Unit
) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Contenedor principal similar al wireframe
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { onNavigate(Screen.TesoreriaMovimientos.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Text("MOVIMIENTOS", style = MaterialTheme.typography.titleMedium)
                    }
                    // Botón Informes
                    Button(
                        onClick = { onNavigate(Screen.TesoreriaInformes.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Text("INFORMES", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }

