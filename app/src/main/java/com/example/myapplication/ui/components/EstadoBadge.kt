package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.EstadoReserva
import com.example.myapplication.ui.theme.EstadoAprobada
import com.example.myapplication.ui.theme.EstadoPagada
import com.example.myapplication.ui.theme.EstadoPendiente
import com.example.myapplication.ui.theme.EstadoRechazada

@Composable
fun EstadoBadge(estado: EstadoReserva) {
    val color = when (estado) {
        EstadoReserva.PENDIENTE -> EstadoPendiente
        EstadoReserva.APROBADA -> EstadoAprobada
        EstadoReserva.PAGADA -> EstadoPagada
        EstadoReserva.RECHAZADA, EstadoReserva.CANCELADA -> EstadoRechazada
    }

    Surface(
        color = color.copy(alpha = 0.15f),   // fondo suave del mismo color
        shape = RoundedCornerShape(50)       // forma de píldora
    ) {
        Text(
            text = estado.name,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}