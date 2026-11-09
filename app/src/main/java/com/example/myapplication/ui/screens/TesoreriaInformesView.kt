package com.example.myapplication.ui.View

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.viewmodel.TesoreriaViewModel

@Composable
fun TesoreriaInformesScreen(viewModel: TesoreriaViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Informes Financieros - Población Zaror", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Resumen General", style = MaterialTheme.typography.titleMedium)
                Text(text = "Total Ingresos: $ ${viewModel.totalIngresos}", color = MaterialTheme.colorScheme.primary)
                Text(text = "Total Egresos: $ ${viewModel.totalEgresos}", color = MaterialTheme.colorScheme.error)
                HorizontalDivider()
                Text(
                    text = "Saldo Líquido Actual: $ ${viewModel.saldoTotal}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
