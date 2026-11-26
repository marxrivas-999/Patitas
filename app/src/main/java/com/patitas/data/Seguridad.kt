package com.patitas.data

import java.security.MessageDigest

object Seguridad {
    // SHA-256 en hexadecimal: así se guarda la contraseña (nunca en texto plano)
    fun hash(texto: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(texto.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
