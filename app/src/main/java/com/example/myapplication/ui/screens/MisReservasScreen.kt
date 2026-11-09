package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.model.EstadoReserva
import com.example.myapplication.model.Usuario
import com.example.myapplication.ui.components.EstadoBadge
import com.example.myapplication.ui.viewmodel.ReservaViewModel

@Composable
fun MisReservasScreen(
    usuario: Usuario?,
    viewModel: ReservaViewModel = viewModel()
) {
    val misReservas = if (usuario == null) emptyList() else viewModel.reservasDe(usuario)

    if (misReservas.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Aún no tienes reservas")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(misReservas, key = { it.id }) { reserva ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${reserva.fecha} · ${reserva.horario.texto}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            EstadoBadge(reserva.estado)
                        }
                        Text(reserva.motivo, style = MaterialTheme.typography.bodySmall)

                        // Qué puede hacer el vecino según el estado
                        when (reserva.estado) {
                            EstadoReserva.PENDIENTE ->
                                TextButton(onClick = { viewModel.cancelar(reserva) }) {
                                    Text("Cancelar solicitud")
                                }
                            EstadoReserva.APROBADA ->
                                Text(
                                    "¡Aprobada! Realiza el pago y entrega el comprobante a Tesorería.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}