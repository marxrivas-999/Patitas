package com.patitas.domain.model

data class Usuario(
    val idUsuario: Int = 0,
    val nombreUsuario: String = "",
    val contrasenaHash: String = "",
    val nombre: String = "",
    val foto: String = "",
    val recibirNotificaciones: Boolean = true
)
