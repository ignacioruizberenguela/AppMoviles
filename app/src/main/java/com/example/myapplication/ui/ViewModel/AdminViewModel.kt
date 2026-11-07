package com.example.myapplication.ui.ViewModel



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AdminViewModel : ViewModel() {

    // Datos simulados para el panel de administración
    var totalVecinosRegistrados by mutableStateOf(128)
        private set

    var solicitudesPendientesArriendo by mutableStateOf(3)
        private set

    var saldoActualTesoreria by mutableStateOf(450000.0)
        private set
}

    // Función para refrescar datos (simulando una llamada a servidor o BD)
