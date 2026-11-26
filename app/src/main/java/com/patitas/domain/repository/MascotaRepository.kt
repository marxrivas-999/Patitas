package com.patitas.domain.repository

import com.patitas.domain.model.Mascota
import kotlinx.coroutines.flow.Flow

interface MascotaRepository {
    fun obtenerMascotas(idUsuario: Int): Flow<List<Mascota>>
    fun obtenerMascota(id: Int): Flow<Mascota?>
    suspend fun insertarMascota(mascota: Mascota)
    suspend fun actualizarMascota(mascota: Mascota)
    suspend fun deleteMascota(mascota: Mascota)
}
