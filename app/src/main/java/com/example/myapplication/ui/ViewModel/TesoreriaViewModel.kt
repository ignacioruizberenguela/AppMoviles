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

    var descripcionInput by mutableStateOf("")
        private set

    var montoInput by mutableStateOf("")
        private set

    // Lista general de movimientos
    val movimientos = mutableStateListOf<Movimiento>()

    // Listas filtradas automáticas
    val ingresos: List<Movimiento>
        get() = movimientos.filter { it.tipo == TipoMovimiento.INGRESO }

    val egresos: List<Movimiento>
        get() = movimientos.filter { it.tipo == TipoMovimiento.EGRESO }

    // Totales para informes
    val totalIngresos: Double
        get() = ingresos.sumOf { it.monto }

    val totalEgresos: Double
        get() = egresos.sumOf { it.monto }

    val saldoTotal: Double
        get() = totalIngresos - totalEgresos

    fun onDescripcionChange(newValue: String) {
        descripcionInput = newValue
    }

    fun onMontoChange(newValue: String) {
        montoInput = newValue
    }

    fun agregarMovimiento(tipo: TipoMovimiento, categoria: Categoria) {
        val montoDouble = montoInput.toDoubleOrNull()
        if (descripcionInput.isNotBlank() && montoDouble != null && montoDouble > 0.0) {
            val nuevoMovimiento = Movimiento(
                tipo = tipo,
                categoria = categoria,
                monto = montoDouble,
                fecha = "2026-06-06", // Fecha simulada o actual
                descripcion = descripcionInput
            )
            movimientos.add(0, nuevoMovimiento)
            descripcionInput = ""
            montoInput = ""
        }
    }
}
