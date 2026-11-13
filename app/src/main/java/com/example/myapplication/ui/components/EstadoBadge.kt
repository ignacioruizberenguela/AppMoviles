package com.example.myapplication.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.EstadoReserva
import com.example.myapplication.ui.theme.EstadoAprobada
import com.example.myapplication.ui.theme.EstadoPagada
import com.example.myapplication.ui.theme.EstadoPendiente
import com.example.myapplication.ui.theme.EstadoRechazada

@Composable
fun EstadoBadge(estado: EstadoReserva) {
    val colorObjetivo = when (estado) {
        EstadoReserva.PENDIENTE -> EstadoPendiente
        EstadoReserva.APROBADA -> EstadoAprobada
        EstadoReserva.PAGADA -> EstadoPagada
        EstadoReserva.RECHAZADA, EstadoReserva.CANCELADA -> EstadoRechazada
    }

    // Cuando cambia el estado, el color pasa suavemente al nuevo (medio segundo)
    val color by animateColorAsState(
        targetValue = colorObjetivo,
        animationSpec = tween(500),
        label = "colorEstado"
    )

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