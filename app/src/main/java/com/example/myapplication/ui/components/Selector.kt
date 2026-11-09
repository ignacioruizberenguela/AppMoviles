package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

// menu desplegable reutilizable para cualquier lista (Rol, Categoria, etc.)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> Selector(
    label: String,
    opciones: List<T>,
    seleccionado: T,
    texto: (T) -> String,
    onSeleccion: (T) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = abierto,
        onExpandedChange = { abierto = it }
    ) {
        OutlinedTextField(
            value = texto(seleccionado),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = abierto) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(texto(opcion)) },
                    onClick = {
                        onSeleccion(opcion)
                        abierto = false
                    }
                )
            }
        }
    }
}