package com.patitas.data.repository

import com.patitas.data.database.dao.RelacionesDao
import com.patitas.data.mapper.toDomain
import com.patitas.domain.model.MascotaConUsuario
import com.patitas.domain.model.UsuarioConMascotas
import com.patitas.domain.repository.RelacionesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RelacionesRepositoryImp @Inject constructor(
    private val dao: RelacionesDao
) : RelacionesRepository {

    override fun usuariosConMascotas(): Flow<List<UsuarioConMascotas>> =
        dao.obtenerUsuariosConMascotas().map { lista -> lista.map { it.toDomain() } }

    override fun mascotasConUsuario(): Flow<List<MascotaConUsuario>> =
        dao.obtenerMascotasConUsuario().map { lista -> lista.map { it.toDomain() } }
}
