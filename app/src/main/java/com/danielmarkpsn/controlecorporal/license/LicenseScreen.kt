package com.danielmarkpsn.controlecorporal.license

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun LicenseScreen(onActivated:()->Unit) {
    val context=LocalContext.current
    var key by remember{mutableStateOf("")}
    var error by remember{mutableStateOf<String?>(null)}

    Column(Modifier.fillMaxSize().padding(24.dp),verticalArrangement=Arrangement.Center) {
        Text("Ativação do aplicativo",style=MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Text("Digite a chave de licença para liberar o Controle Corporal.")
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value=key,
            onValueChange={key=it;error=null},
            modifier=Modifier.fillMaxWidth(),
            label={Text("Chave de licença")},
            minLines=4,
            keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Ascii)
        )
        Spacer(Modifier.height(12.dp))
        Text("ID deste aparelho: ${LicenseManager.getDeviceId(context)}")
        Spacer(Modifier.height(18.dp))
        Button(
            onClick={
                val result=LicenseManager.activate(context,key)
                if(result.isSuccess) onActivated() else error=result.exceptionOrNull()?.message
            },
            enabled=key.isNotBlank(),
            modifier=Modifier.fillMaxWidth()
        ){ Text("ATIVAR LICENÇA") }
        error?.let{
            Spacer(Modifier.height(12.dp))
            Text(it,color=MaterialTheme.colorScheme.error)
        }
    }
}
