package com.patitas.data.repository

import com.patitas.data.database.dao.MascotaDao
import com.patitas.data.mapper.toDomain
import com.patitas.data.mapper.toEntity
import com.patitas.domain.model.Mascota
import com.patitas.domain.repository.MascotaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MascotaRepositoryImp @Inject constructor(
    private val mascotaDao: MascotaDao
) : MascotaRepository {

    override fun obtenerMascotas(idUsuario: Int): Flow<List<Mascota>> {
        return mascotaDao.obtenerMascotas(idUsuario).map { lista -> lista.map { it.toDomain() } }
    }

    override fun obtenerMascota(id: Int): Flow<Mascota?> {
        return mascotaDao.getMascota(id).map { it?.toDomain() }
    }

    override suspend fun insertarMascota(mascota: Mascota) {
        mascotaDao.insertarMascota(mascota.toEntity())
    }

    override suspend fun actualizarMascota(mascota: Mascota) {
        mascotaDao.actualizarMascota(mascota.toEntity())
    }

    override suspend fun deleteMascota(mascota: Mascota) {
        mascotaDao.eliminarMascota(mascota.toEntity())
    }
}
