package com.patitas.domain.model


data class UsuarioConMascotas(
    val usuario: Usuario,
    val mascotas: List<Mascota>
)
