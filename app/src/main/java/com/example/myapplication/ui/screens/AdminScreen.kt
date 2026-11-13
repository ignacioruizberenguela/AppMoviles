package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.model.*
import com.example.myapplication.ui.components.Selector
import com.example.myapplication.ui.viewmodel.AdminViewModel

@Composable
fun AdminScreen(viewModel: AdminViewModel = viewModel()) {
    var pestana by remember { mutableStateOf(0) }
    val pestanas = listOf("Resumen", "Usuarios", "Reservas", "Movimientos")

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = pestana, edgePadding = 0.dp) {
            pestanas.forEachIndexed { indice, titulo ->
                Tab(
                    selected = pestana == indice,
                    onClick = { pestana = indice },
                    text = { Text(titulo) }
                )
            }
        }

        when (pestana) {
            0 -> ResumenTab(viewModel)
            1 -> UsuariosTab(viewModel)
            2 -> ReservasTab(viewModel)
            3 -> MovimientosScreen()
        }
    }
}

// ==================== RESUMEN ====================
@Composable
private fun ResumenTab(vm: AdminViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TarjetaDato("Vecinos registrados", "${vm.totalVecinos}")
        TarjetaDato("Solicitudes pendientes", "${vm.solicitudesPendientes}")
        TarjetaDato("Saldo en Tesorería", "$ ${vm.saldo}")
    }
}

@Composable
private fun TarjetaDato(titulo: String, valor: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.labelLarge)
            Text(
                valor,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ==================== USUARIOS ====================
@Composable
private fun UsuariosTab(vm: AdminViewModel) {
    // null = diálogo cerrado · id 0 = nuevo · otro id = editando
    var editando by remember { mutableStateOf<Usuario?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = { editando = Usuario(nombre = "", rut = "", email = "", rol = Rol.VECINO) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Nuevo usuario")
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(vm.usuarios, key = { it.id }) { usuario ->
                FilaCrud(
                    titulo = usuario.nombre,
                    subtitulo = "${usuario.email} · ${usuario.rol.nombre}",
                    onEditar = { editando = usuario },
                    onEliminar = { vm.eliminarUsuario(usuario) }
                )
            }
        }
    }

    editando?.let { usuario ->
        UsuarioDialog(
            usuario = usuario,
            onGuardar = {
                vm.guardarUsuario(it)
                editando = null
            },
            onCerrar = { editando = null }
        )
    }
}

@Composable
private fun UsuarioDialog(
    usuario: Usuario,
    onGuardar: (Usuario) -> Unit,
    onCerrar: () -> Unit
) {
    var nombre by remember { mutableStateOf(usuario.nombre) }
    var rut by remember { mutableStateOf(usuario.rut) }
    var email by remember { mutableStateOf(usuario.email) }
    var rol by remember { mutableStateOf(usuario.rol) }

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(if (usuario.id == 0) "Nuevo usuario" else "Editar usuario") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = rut,
                    onValueChange = { rut = it },
                    label = { Text("RUT") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Selector(
                    label = "Rol",
                    opciones = Rol.entries,
                    seleccionado = rol,
                    texto = { it.nombre },
                    onSeleccion = { rol = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onGuardar(usuario.copy(nombre = nombre, rut = rut, email = email, rol = rol))
                },
                enabled = nombre.isNotBlank() && email.isNotBlank()
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) { Text("Cancelar") }
        }
    )
}

// ==================== RESERVAS ====================
@Composable
private fun ReservasTab(vm: AdminViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(vm.reservas, key = { it.id }) { reserva ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(reserva.vecinoNombre, style = MaterialTheme.typography.titleMedium)
                            Text("${reserva.fecha} · ${reserva.horario.texto}", style = MaterialTheme.typography.bodySmall)
                            Text(reserva.motivo, style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { vm.eliminarReserva(reserva) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    Selector(
                        label = "Estado",
                        opciones = EstadoReserva.entries,
                        seleccionado = reserva.estado,
                        texto = { it.name },
                        onSeleccion = { vm.cambiarEstadoReserva(reserva, it) }
                    )
                }
            }
        }
    }
}

// ==================== COMPONENTE COMÚN ====================
@Composable
private fun FilaCrud(
    titulo: String,
    subtitulo: String,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, style = MaterialTheme.typography.titleMedium)
                Text(subtitulo, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onEditar) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onEliminar) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}