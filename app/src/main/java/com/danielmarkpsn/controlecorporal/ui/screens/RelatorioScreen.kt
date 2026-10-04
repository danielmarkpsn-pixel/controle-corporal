package com.danielmarkpsn.controlecorporal.ui.screens

import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.danielmarkpsn.controlecorporal.data.Medicao
import com.danielmarkpsn.controlecorporal.data.MetaEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelatorioScreen(medicoes: List<Medicao>, meta: MetaEntity?, onVoltar: () -> Unit) {
    val context = LocalContext.current
    var pending by remember { mutableStateOf<ByteArray?>(null) }
    val creator = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri ->
        if (uri != null && pending != null) context.contentResolver.openOutputStream(uri)?.use { it.write(pending) }
        pending = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório") },
                navigationIcon = { IconButton(onClick = onVoltar) { Icon(Icons.Default.ArrowBack, contentDescription = "Voltar") } }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Relatório de evolução corporal", style = MaterialTheme.typography.headlineSmall)
            Text("${medicoes.size} registros de acompanhamento.")
            Text("O PDF inclui peso, altura, meta e todas as medidas por lado.")
            Button(
                onClick = {
                    pending = gerarPdf(medicoes, meta)
                    creator.launch("relatorio-controle-corporal.pdf")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Exportar PDF")
            }
        }
    }
}

private fun gerarPdf(medicoes: List<Medicao>, meta: MetaEntity?): ByteArray {
    val doc = PdfDocument()
    val normal = Paint().apply { textSize = 12f }
    val title = Paint(normal).apply { textSize = 20f; isFakeBoldText = true }
    val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var pageNumber = 1
    var page = doc.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNumber).create())
    var y = 50f

    fun line(text: String, paint: Paint = normal) {
        if (y > 790f) {
            doc.finishPage(page)
            pageNumber++
            page = doc.startPage(PdfDocument.PageInfo.Builder(595, 842, pageNumber).create())
            y = 50f
        }
        page.canvas.drawText(text, 40f, y, paint)
        y += 22f
    }

    line("Controle Corporal", title)
    line("Relatório de evolução")
    line("")
    meta?.alturaCm?.takeIf { it > 0 }?.let { line("Altura: %.1f cm".format(it)) }
    meta?.pesoAlvoKg?.takeIf { it > 0 }?.let { line("Meta de peso: %.1f kg".format(it)) }
    line("")

    medicoes.sortedBy { it.data }.forEach { m ->
        line("${date.format(Date(m.data))}  |  Peso: %.1f kg".format(m.peso))
        if (m.cintura > 0) line("  Cintura: %.1f cm".format(m.cintura))
        if (m.quadril > 0) line("  Quadril: %.1f cm".format(m.quadril))
        if (m.peito > 0) line("  Peito: %.1f cm".format(m.peito))
        if (m.bracoDireito > 0 || m.bracoEsquerdo > 0) line("  Braços: D %.1f | E %.1f cm".format(m.bracoDireito, m.bracoEsquerdo))
        if (m.coxaDireita > 0 || m.coxaEsquerda > 0) line("  Coxas: D %.1f | E %.1f cm".format(m.coxaDireita, m.coxaEsquerda))
        if (m.panturrilhaDireita > 0 || m.panturrilhaEsquerda > 0) line("  Panturrilhas: D %.1f | E %.1f cm".format(m.panturrilhaDireita, m.panturrilhaEsquerda))
        line("")
    }

    doc.finishPage(page)
    val out = java.io.ByteArrayOutputStream()
    doc.writeTo(out)
    doc.close()
    return out.toByteArray()
}
