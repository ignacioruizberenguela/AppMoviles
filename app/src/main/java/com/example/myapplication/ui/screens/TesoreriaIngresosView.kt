package com.example.myapplication.ui.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.viewmodel.TesoreriaViewModel

@Composable
fun TesoreriaIngresosScreen(viewModel: TesoreriaViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Registro de Ingresos", fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)

        // Formulario rápido para añadir ingreso
        OutlinedTextField(
            value = viewModel.descripcionInput,
            onValueChange = { viewModel.onDescripcionChange(it) },
            label = { Text("Descripción del Ingreso (ej. Cuota Vecino)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.montoInput,
            onValueChange = { viewModel.onMontoChange(it) },
            label = { Text("Monto ($)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.agregarMovimiento(com.example.myapplication.model.TipoMovimiento.INGRESO, com.example.myapplication.model.Categoria.OTRO) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Ingreso")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(text = "Historial de Ingresos", style = MaterialTheme.typography.titleMedium)

        // Lista de ingresos filtrados
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.ingresos) { movimiento ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = movimiento.descripcion, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Monto: $ ${movimiento.monto}", color = MaterialTheme.colorScheme.primary)
                        Text(text = "Fecha: ${movimiento.fecha}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
