package com.example.myapplication.ui.screens



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VecinoFormularioScreen(
    onSubmitSuccess: () -> Unit,
) {
    var fecha by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var comprobanteAdjunto by remember { mutableStateOf(false) }

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

            Spacer(modifier = Modifier.height(24.dp))

            // Fecha del Arriendo
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha de Arriendo (DD/MM/AAAA)") },
                singleLine = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendario")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Horario
            OutlinedTextField(
                value = horario,
                onValueChange = { horario = it },
                label = { Text("Horario (Ej: 15:00 - 20:00 hrs)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Motivo del Evento
            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo / Actividad (Cumpleaños, Reunión, etc.)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sección para adjuntar comprobante usando la cámara (Recurso nativo sugerido)
            OutlinedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Respaldo de Pago / Comprobante",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            // Aquí simularíamos la llamada a la cámara del dispositivo
                            comprobanteAdjunto = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Cámara")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (comprobanteAdjunto) "¡Comprobante Adjuntado!" else "Tomar Foto con Cámara")
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Botón de Enviar Solicitud
            Button(
                onClick = { onSubmitSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "ENVIAR SOLICITUD")
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun VecinoFormularioPreview() {
    MyApplicationTheme {
        VecinoFormularioScreen(
            onSubmitSuccess = {}
        )
    }
}
