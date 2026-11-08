package com.example.myapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Movimiento
import com.example.myapplication.model.TipoMovimiento
import com.example.myapplication.model.Categoria 

class TesoreriaViewModel : ViewModel() {

    // Inputs para la interfaz
    var descripcionInput by mutableStateOf("")
        private set

    var montoInput by mutableStateOf("")
        private set

    // Lista reactiva basada en tu entidad Movimiento
    var movimientos = mutableStateListOf<Movimiento>()
        private set

    // Saldo total calculado automáticamente
    val saldoTotal: Double
        get() = movimientos.sumOf {
            if (it.tipo == TipoMovimiento.INGRESO) it.monto else -it.monto
        }

    fun onDescripcionChange(newValue: String) {
        descripcionInput = newValue
    }

    fun onMontoChange(newValue: String) {
        montoInput = newValue
    }

    // Registrar un nuevo movimiento (Ingreso o Egreso)
    fun agregarMovimiento(tipo: TipoMovimiento, categoria: Categoria) {
        val montoDouble = montoInput.toDoubleOrNull()
        if (descripcionInput.isNotBlank() && montoDouble != null && montoDouble > 0.0) {
            val nuevoMovimiento = Movimiento(
                tipo = tipo,
                categoria = categoria,
                monto = montoDouble,
                fecha = "2026-06-06",
                descripcion = descripcionInput
            )

            movimientos.add(0, nuevoMovimiento) 

            // Limpiar campos
            descripcionInput = ""
            montoInput = ""
        }
    }
}
