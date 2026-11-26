package com.patitas.data.repository

import com.patitas.data.mapper.toDomain
import com.patitas.data.network.RazaApiService
import com.patitas.domain.repository.RazaRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RazaRepositoryImp @Inject constructor(
    private val apiService: RazaApiService
) : RazaRepository {

    override suspend fun obtenerRazasPerro(): Result<List<String>> {
        // try-catch para manejar errores de red o del servidor
        return try {
            Result.success(apiService.getRazasPerro().toDomain())
        } catch (e: CancellationException) {
            throw e   // si se cancela la corrutina no es un error de red
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
