package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.model.Horario
import com.example.myapplication.model.Usuario
import com.example.myapplication.ui.viewmodel.ReservaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VecinoFormularioScreen(
    usuario: Usuario?,
    onSubmitSuccess: () -> Unit,
    viewModel: ReservaViewModel = viewModel()
) {
    var fecha by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf(Horario.TARDE) }
    var motivo by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var mostrarCalendario by remember { mutableStateOf(false) }
    val estadoCalendario = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solicitud de Dependencias",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Sede: Los Jazmines 425, Cerrillos",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        usuario?.let {
            Text(text = "Solicitante: ${it.nombre}", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ---------- Fecha (se elige con el calendario) ----------
        OutlinedTextField(
            value = fecha,
            onValueChange = {},
            readOnly = true,
            label = { Text("Fecha de arriendo") },
            placeholder = { Text("Toca el calendario →") },
            trailingIcon = {
                IconButton(onClick = { mostrarCalendario = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Elegir fecha")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- Horario (bloques fijos) ----------
        Text(
            text = "Horario",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Horario.entries.forEach { opcion ->
                val ocupado = fecha.isNotBlank() && viewModel.estaOcupado(fecha, opcion)
                FilterChip(
                    selected = horario == opcion,
                    onClick = { horario = opcion },
                    enabled = !ocupado,
                    label = { Text(if (ocupado) "${opcion.texto} (ocupado)" else opcion.texto) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = motivo,
            onValueChange = { motivo = it },
            label = { Text("Motivo / Actividad (Cumpleaños, Reunión, etc.)") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 4
        )

        // error animado
        AnimatedVisibility(visible = error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (usuario == null) {
                    error = "Debes iniciar sesión"
                } else {
                    val resultado = viewModel.enviarSolicitud(usuario, fecha, horario, motivo)
                    if (resultado == null) onSubmitSuccess() else error = resultado
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "ENVIAR SOLICITUD")
        }
    }

    // ---------- Calendario (aparece al tocar el ícono) ----------
    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    estadoCalendario.selectedDateMillis?.let { fecha = millisAFecha(it) }
                    mostrarCalendario = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = estadoCalendario)
        }
    }
}

// El calendario entrega milisegundos en UTC → lo pasamos a "yyyy-MM-dd"
private fun millisAFecha(millis: Long): String {
    val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    formato.timeZone = TimeZone.getTimeZone("UTC")
    return formato.format(Date(millis))
}