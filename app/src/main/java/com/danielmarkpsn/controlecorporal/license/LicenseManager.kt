package com.danielmarkpsn.controlecorporal.license

import android.content.Context
import android.provider.Settings
import android.util.Base64
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.time.LocalDate

data class LicenseData(val product:String,val customer:String,val expires:LocalDate,val device:String)

object LicenseManager {
    private const val PREFS="controle_corporal_license"
    private const val KEY="license_key"

    fun isLicensed(context:Context):Boolean =
        context.getSharedPreferences(PREFS,0).getString(KEY,null)?.let { validate(context,it).isSuccess } ?: false

    fun activate(context:Context,key:String):Result<LicenseData> {
        val result=validate(context,key.trim())
        if(result.isSuccess) context.getSharedPreferences(PREFS,0).edit().putString(KEY,key.trim()).apply()
        return result
    }

    fun getDeviceId(context:Context):String =
        Settings.Secure.getString(context.contentResolver,Settings.Secure.ANDROID_ID) ?: "UNKNOWN"

    private fun validate(context:Context,key:String):Result<LicenseData> {
        if(key.isBlank()) return Result.failure(Exception("Informe a chave de licença."))
        val parts=key.split(".")
        if(parts.size!=2) return Result.failure(Exception("Chave de licença inválida."))
        return try {
            val payload=decode(parts[0])
            val signature=decode(parts[1])
            val verifier=Signature.getInstance("SHA256withRSA")
            verifier.initVerify(publicKey())
            verifier.update(payload)
            if(!verifier.verify(signature)) return Result.failure(Exception("Assinatura da licença inválida."))
            val values=payload.toString(Charsets.UTF_8).split("|").mapNotNull {
                val p=it.indexOf("="); if(p>0) it.substring(0,p) to it.substring(p+1) else null
            }.toMap()
            if(values["product"]!=LicenseConfig.PRODUCT_ID) return Result.failure(Exception("Licença de outro aplicativo."))
            val expires=LocalDate.parse(values["expires"])
            if(LocalDate.now().isAfter(expires)) return Result.failure(Exception("Esta licença está expirada."))
            val device=values["device"].orEmpty()
            val current=getDeviceId(context)
            if(device.isNotBlank() && device!="*" && !device.equals(current,true))
                return Result.failure(Exception("Esta licença pertence a outro aparelho."))
            Result.success(LicenseData(values["product"].orEmpty(),values["customer"].orEmpty(),expires,device))
        } catch(e:Exception) { Result.failure(Exception("Não foi possível validar a licença.")) }
    }

    private fun decode(value:String)=Base64.decode(value,Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)

    private fun publicKey()=KeyFactory.getInstance("RSA").generatePublic(
        X509EncodedKeySpec(Base64.decode(
            LicenseConfig.PUBLIC_KEY_PEM.replace("-----BEGIN PUBLIC KEY-----","").replace("-----END PUBLIC KEY-----","").replace("\\s".toRegex(),""),
            Base64.DEFAULT))
    )
}
