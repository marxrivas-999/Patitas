package com.patitas.data

import java.security.MessageDigest

object Seguridad {

    fun hash(texto: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(texto.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
