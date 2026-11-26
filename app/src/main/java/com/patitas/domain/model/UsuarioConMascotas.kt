package com.patitas.domain.model

// Un usuario junto con todas sus mascotas (resultado de la relación 1 a N)
data class UsuarioConMascotas(
    val usuario: Usuario,
    val mascotas: List<Mascota>
)
