package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.model.Reserva
import com.example.myapplication.model.Horario



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectivaSecretariaScreen(
) {
    // Lista de ejemplo basada en los 2 a 3 arriendos mensuales del caso
    val solicitudes = listOf(
        Reserva(1, 10, "María González", "15/11/2026", Horario.TARDE, "Cumpleaños familiar"),
        Reserva(2, 11, "Carlos Soto", "22/11/2026", Horario.MANANA, "Reunión de comité")
    )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Revisiones Pendientes",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Verifica disponibilidad de fechas y horarios de arriendo de la sede.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista dinámica de solicitudes
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(solicitudes) { solicitud ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Solicitante: ${solicitud.vecinoNombre}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Fecha: ${solicitud.fecha} (${solicitud.horario})")
                            Text(text = "Motivo: ${solicitud.motivo}")
                            Text(text = "Estado: ${solicitud.estado}", color = MaterialTheme.colorScheme.secondary)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = { /* Lógica para rechazar */ },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Icon(imageVector = Icons.Default.Cancel, contentDescription = "Rechazar")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Rechazar")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { /* Lógica para aprobar */ }
                                ) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Aprobar")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Aprobar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun DirectivaSecretariaPreview() {
    MyApplicationTheme {
        DirectivaSecretariaScreen(
        )
    }
}
