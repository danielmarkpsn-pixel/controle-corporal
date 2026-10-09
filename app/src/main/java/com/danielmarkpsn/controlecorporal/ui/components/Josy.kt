package com.danielmarkpsn.controlecorporal.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.R
import com.danielmarkpsn.controlecorporal.ui.theme.Lilas
import com.danielmarkpsn.controlecorporal.ui.theme.LilasClaro

@Composable
fun JosyHero(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Card(modifier = modifier, shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(Modifier.fillMaxWidth().heightIn(min = 175.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f).padding(start = 20.dp, top = 18.dp, bottom = 18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = Lilas.copy(alpha = .16f), modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Lilas, modifier = Modifier.padding(7.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("JOSY • SUA PARCEIRA", color = LilasClaro, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                }
                Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (actionLabel != null && onAction != null) {
                    FilledTonalButton(onClick = onAction) { Text(actionLabel) }
                }
            }
            Image(
                painter = painterResource(R.drawable.josy_mascote),
                contentDescription = "Josy, mascote do Controle Corporal",
                contentScale = ContentScale.Fit,
                modifier = Modifier.width(145.dp).height(190.dp)
            )
        }
    }
}

@Composable
fun JosyMini(modifier: Modifier = Modifier, message: String = "Vamos evoluir juntos!") {
    Row(modifier.clip(RoundedCornerShape(18.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(R.drawable.josy_mascote), "Josy", contentScale = ContentScale.Fit, modifier = Modifier.size(width = 66.dp, height = 76.dp).clip(RoundedCornerShape(12.dp)))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text("Josy", fontWeight = FontWeight.Bold)
            Text(message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Default.FitnessCenter, null, tint = Lilas)
    }
}
