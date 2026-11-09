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
fun TesoreriaEgresosScreen(viewModel: TesoreriaViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Registro de Egresos", fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = viewModel.descripcionInput,
            onValueChange = { viewModel.onDescripcionChange(it) },
            label = { Text("Descripción del Gasto (ej. Compra Insumos)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.montoInput,
            onValueChange = { viewModel.onMontoChange(it) },
            label = { Text("Monto ($)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.agregarMovimiento(com.example.myapplication.model.TipoMovimiento.EGRESO, com.example.myapplication.model.Categoria.OTRO) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Egreso")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text(text = "Historial de Egresos", style = MaterialTheme.typography.titleMedium)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(viewModel.egresos) { movimiento ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = movimiento.descripcion, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Monto: $ ${movimiento.monto}", color = MaterialTheme.colorScheme.error)
                        Text(text = "Fecha: ${movimiento.fecha}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
