package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

// foto de comprobante a partir de su URI
@Composable
fun ComprobanteImagen(uri: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = uri,
        contentDescription = "Comprobante de pago",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
    )
}