package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.example.myapplication.ui.theme.RojoEgreso
import com.example.myapplication.ui.theme.VerdeIngreso
import com.example.myapplication.ui.viewmodel.TesoreriaViewModel
import com.example.myapplication.ui.viewmodel.formatoPesos

@Composable
fun MovimientosScreen(viewModel: TesoreriaViewModel = viewModel()) {
    // null = diálogo cerrado · id 0 = nuevo · otro id = editando
    var editando by remember { mutableStateOf<Movimiento?>(null) }
    val lista = viewModel.movimientosFiltrados

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ---------- Filtro por tipo ----------
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = viewModel.filtroTipo == null,
                onClick = { viewModel.cambiarFiltroTipo(null) },
                label = { Text("Todos") }
            )
            FilterChip(
                selected = viewModel.filtroTipo == TipoMovimiento.INGRESO,
                onClick = { viewModel.cambiarFiltroTipo(TipoMovimiento.INGRESO) },
                label = { Text("Ingresos") }
            )
            FilterChip(
                selected = viewModel.filtroTipo == TipoMovimiento.EGRESO,
                onClick = { viewModel.cambiarFiltroTipo(TipoMovimiento.EGRESO) },
                label = { Text("Egresos") }
            )
        }

        // ---------- Filtro por categoría ----------
        // null al principio = opción "Todas"
        val categorias: List<Categoria?> =
            listOf(null) + (viewModel.filtroTipo?.let { viewModel.categoriasPara(it) } ?: Categoria.entries)

        Selector(
            label = "Categoría",
            opciones = categorias,
            seleccionado = viewModel.filtroCategoria,
            texto = { it?.name ?: "Todas" },
            onSeleccion = { viewModel.cambiarFiltroCategoria(it) }
        )

        // ---------- Botón nuevo ----------
        Button(
            onClick = {
                editando = Movimiento(
                    tipo = viewModel.filtroTipo ?: TipoMovimiento.INGRESO,
                    categoria = Categoria.OTRO,
                    monto = 0,
                    fecha = "",
                    descripcion = ""
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Nuevo movimiento")
        }

        Text(
            text = "${lista.size} movimientos",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // ---------- Lista ----------
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(lista, key = { it.id }) { mov ->
                MovimientoItem(
                    mov = mov,
                    onEditar = { editando = mov },
                    onEliminar = { viewModel.eliminarMovimiento(mov) }
                )
            }
        }
    }

    editando?.let { mov ->
        MovimientoDialog(
            movimiento = mov,
            categoriasPara = { viewModel.categoriasPara(it) },
            reservasPorPagar = viewModel.reservasPorPagar,
            onGuardar = {
                viewModel.guardarMovimiento(it)
                editando = null
            },
            onCerrar = { editando = null }
        )
    }
}

// Una fila de la lista: verde si es ingreso, rojo si es egreso
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovimientoItem(
    mov: Movimiento,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val esIngreso = mov.tipo == TipoMovimiento.INGRESO
    val color = if (esIngreso) VerdeIngreso else RojoEgreso
    val signo = if (esIngreso) "+" else "-"

    Card(onClick = onEditar, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barrita de color a la izquierda
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(mov.descripcion, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${mov.fecha} · ${mov.categoria.name}",
                    style = MaterialTheme.typography.bodySmall
                )
                if (mov.reservaId != null) {
                    Text(
                        "Pago de reserva #${mov.reservaId}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (mov.comprobanteUri != null) {
                    Text("📎 Con comprobante", style = MaterialTheme.typography.bodySmall)
                }
            }

            Text(
                text = "$signo ${formatoPesos(mov.monto)}",
                color = color,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onEliminar) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Diálogo para crear o editar un movimiento
@Composable
private fun MovimientoDialog(
    movimiento: Movimiento,
    categoriasPara: (TipoMovimiento) -> List<Categoria>,
    reservasPorPagar: List<Reserva>,
    onGuardar: (Movimiento) -> Unit,
    onCerrar: () -> Unit
) {
    var tipo by remember { mutableStateOf(movimiento.tipo) }
    var categoria by remember { mutableStateOf(movimiento.categoria) }
    var monto by remember { mutableStateOf(if (movimiento.monto == 0) "" else movimiento.monto.toString()) }
    var descripcion by remember { mutableStateOf(movimiento.descripcion) }
    var reservaId by remember { mutableStateOf(movimiento.reservaId) }

    val esPagoArriendo = tipo == TipoMovimiento.INGRESO && categoria == Categoria.ARRIENDO

    AlertDialog(
        onDismissRequest = onCerrar,
        title = { Text(if (movimiento.id == 0) "Nuevo movimiento" else "Editar movimiento") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Selector(
                    label = "Tipo",
                    opciones = TipoMovimiento.entries,
                    seleccionado = tipo,
                    texto = { it.name },
                    onSeleccion = {
                        tipo = it
                        categoria = categoriasPara(it).first()  // categoría válida para el nuevo tipo
                    }
                )
                Selector(
                    label = "Categoría",
                    opciones = categoriasPara(tipo),
                    seleccionado = categoria,
                    texto = { it.name },
                    onSeleccion = { categoria = it }
                )

                // Solo si es un ingreso de arriendo: enlazar con una reserva aprobada
                if (esPagoArriendo) {
                    val opciones: List<Reserva?> = listOf(null) + reservasPorPagar
                    Selector(
                        label = "Reserva",
                        opciones = opciones,
                        seleccionado = opciones.find { it?.id == reservaId },
                        texto = { r -> r?.let { "#${it.id} ${it.vecinoNombre} (${it.fecha})" } ?: "Sin reserva" },
                        onSeleccion = { reservaId = it?.id }
                    )
                }

                OutlinedTextField(
                    value = monto,
                    onValueChange = { nuevo -> if (nuevo.all { it.isDigit() }) monto = nuevo },
                    label = { Text("Monto (CLP)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onGuardar(
                        movimiento.copy(
                            tipo = tipo,
                            categoria = categoria,
                            monto = monto.toInt(),
                            descripcion = descripcion,
                            reservaId = if (esPagoArriendo) reservaId else null
                        )
                    )
                },
                enabled = (monto.toIntOrNull() ?: 0) > 0 && descripcion.isNotBlank()
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) { Text("Cancelar") }
        }
    )
}