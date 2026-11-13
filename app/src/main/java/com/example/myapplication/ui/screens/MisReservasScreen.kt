package com.example.myapplication.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.model.EstadoReserva
import com.example.myapplication.model.Reserva
import com.example.myapplication.model.Usuario
import com.example.myapplication.ui.components.ComprobanteImagen
import com.example.myapplication.ui.components.EstadoBadge
import com.example.myapplication.ui.viewmodel.ReservaViewModel
import java.io.File

@Composable
fun MisReservasScreen(
    usuario: Usuario?,
    viewModel: ReservaViewModel = viewModel()
) {
    val context = LocalContext.current
    val misReservas = if (usuario == null) emptyList() else viewModel.reservasDe(usuario)

    // reserva a la que le estamos sacando foto, y dónde se guardará la foto
    var reservaEnFoto by remember { mutableStateOf<Reserva?>(null) }
    var uriPendiente by remember { mutableStateOf<Uri?>(null) }
    var mensaje by remember { mutableStateOf("") }

    // 1) abre la camara y avisa si se tomo la foto
    val camaraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { fotoTomada ->
        val reserva = reservaEnFoto
        val uri = uriPendiente
        if (fotoTomada && reserva != null && uri != null) {
            viewModel.adjuntarComprobante(reserva, uri.toString())
            mensaje = "Comprobante guardado ✔"
        }
        reservaEnFoto = null
        uriPendiente = null
    }

    fun abrirCamara(reserva: Reserva) {
        val uri = crearUriComprobante(context, reserva.id)
        reservaEnFoto = reserva
        uriPendiente = uri
        camaraLauncher.launch(uri)
    }

    // 2) si no tiene permiso de camara, lo pide:
    val permisoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            reservaEnFoto?.let { abrirCamara(it) }
        } else {
            mensaje = "Sin permiso de cámara no podemos fotografiar el comprobante"
            reservaEnFoto = null
        }
    }

    fun fotografiar(reserva: Reserva) {
        val tienePermiso = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (tienePermiso) {
            abrirCamara(reserva)
        } else {
            reservaEnFoto = reserva
            permisoLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (misReservas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aún no tienes reservas")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(misReservas, key = { it.id }) { reserva ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
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

                            // foto de comprobante (si hay)
                            reserva.comprobanteUri?.let { ComprobanteImagen(uri = it) }

                            when (reserva.estado) {
                                EstadoReserva.PENDIENTE ->
                                    TextButton(onClick = { viewModel.cancelar(reserva) }) {
                                        Text("Cancelar solicitud")
                                    }

                                EstadoReserva.APROBADA -> {
                                    Text(
                                        text = if (reserva.comprobanteUri == null)
                                            "¡Aprobada! Realiza el pago y fotografía el comprobante."
                                        else
                                            "Comprobante enviado. Tesorería confirmará el pago.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    OutlinedButton(onClick = { fotografiar(reserva) }) {
                                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            if (reserva.comprobanteUri == null) "Fotografiar comprobante"
                                            else "Volver a tomar foto"
                                        )
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

// crear archivo vacio en la carpeta privada de la app y devuelve su URI segura
private fun crearUriComprobante(context: Context, reservaId: Int): Uri {
    val carpeta = File(context.filesDir, "comprobantes").apply { mkdirs() }
    val archivo = File(carpeta, "reserva_${reservaId}_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
}