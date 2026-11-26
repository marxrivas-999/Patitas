package com.patitas.domain.model

// Una mascota junto con su dueño (resultado de la relación N a 1)
data class MascotaConUsuario(
    val mascota: Mascota,
    val usuario: Usuario
)
