package com.example.myapplication.ui.Screens



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TesoreriaScreen(
    onNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Tesorería - Población Zaror") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                    // Botón Ingresos
                    Button(
                        onClick = { onNavigate("tesoreria_ingresos") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Text("INGRESOS", style = MaterialTheme.typography.titleMedium)
                    }

                    // Botón Egresos
                    Button(
                        onClick = { onNavigate("tesoreria_egresos") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        Text("EGRESOS", style = MaterialTheme.typography.titleMedium)
                    }

                    // Botón Informes
                    Button(
                        onClick = { onNavigate("tesoreria_informes") },
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
}
