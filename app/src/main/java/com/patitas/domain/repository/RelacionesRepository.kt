package com.patitas.domain.repository

import com.patitas.domain.model.MascotaConUsuario
import com.patitas.domain.model.UsuarioConMascotas
import kotlinx.coroutines.flow.Flow

interface RelacionesRepository {
    fun usuariosConMascotas(): Flow<List<UsuarioConMascotas>>
    fun mascotasConUsuario(): Flow<List<MascotaConUsuario>>
}
