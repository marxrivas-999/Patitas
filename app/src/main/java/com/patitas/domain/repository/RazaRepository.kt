package com.patitas.domain.repository

interface RazaRepository {
    // Lista de razas de perro desde la API; Result.failure si no hay red o el servidor falla
    suspend fun obtenerRazasPerro(): Result<List<String>>
}
