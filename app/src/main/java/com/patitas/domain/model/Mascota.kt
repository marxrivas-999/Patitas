package com.patitas.domain.model

data class Mascota(
    val idMascota: Int = 0,
    val idUsuario: Int = 0,
    val nombre: String = "",
    val especie: String = "",
    val raza: String = "",
    val fechaNacimiento: String = "",   // aaaa-mm-dd
    val pesoKg: Float? = null,
    val foto: String = "",
    val alergias: String = "",
    val notas: String = "",
    val esFavorita: Boolean = false
)
