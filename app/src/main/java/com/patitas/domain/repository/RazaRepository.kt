package com.patitas.domain.repository

interface RazaRepository {

    suspend fun obtenerRazasPerro(): Result<List<String>>
}
