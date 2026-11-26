package com.patitas.data.network.dto

import kotlinx.serialization.Serializable

// Respuesta de https://dog.ceo/api/breeds/list/all
// Ejemplo: {"message": {"bulldog": ["boston", "english", "french"], "akita": []}, "status": "success"}
// La clave es la raza y su lista contiene las subrazas (puede estar vacía).
// (el JSON puede traer más campos; ignoreUnknownKeys = true los descarta)
@Serializable
data class RazasPerroDto(
    val message: Map<String, List<String>> = emptyMap(),
    val status: String = ""
)
