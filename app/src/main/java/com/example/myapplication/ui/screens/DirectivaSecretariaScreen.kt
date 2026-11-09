package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.model.EstadoReserva
import com.example.myapplication.ui.components.EstadoBadge
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodel.ReservaViewModel

@Composable
fun DirectivaSecretariaScreen(viewModel: ReservaViewModel = viewModel()) {
    var soloPendientes by remember { mutableStateOf(true) }
    val lista = if (soloPendientes) viewModel.pendientes else viewModel.reservas

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Verifica la disponibilidad y aprueba o rechaza las solicitudes.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = soloPendientes,
                onClick = { soloPendientes = true },
                label = { Text("Pendientes (${viewModel.pendientes.size})") }
            )
            FilterChip(
                selected = !soloPendientes,
                onClick = { soloPendientes = false },
                label = { Text("Todas") }
            )
        }

        if (lista.isEmpty()) {
            Text("No hay solicitudes", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(lista, key = { it.id }) { solicitud ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = solicitud.vecinoNombre,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            EstadoBadge(solicitud.estado)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Fecha: ${solicitud.fecha} (${solicitud.horario.texto})")
                        Text(text = "Motivo: ${solicitud.motivo}")

                        // Solo las pendientes se pueden aprobar o rechazar
                        if (solicitud.estado == EstadoReserva.PENDIENTE) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.rechazar(solicitud) },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(Icons.Default.Cancel, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Rechazar")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { viewModel.aprobar(solicitud) }) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null)
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
}

@Preview(showBackground = true)
@Composable
fun DirectivaSecretariaPreview() {
    MyApplicationTheme {
        DirectivaSecretariaScreen()
    }
}