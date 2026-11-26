package com.patitas.domain.repository

import com.patitas.domain.model.Usuario
import com.patitas.domain.model.UsuarioConMascotas
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {

    suspend fun iniciarSesion(nombreUsuario: String, contrasena: String): Usuario?
    fun obtenerUsuario(id: Int): Flow<Usuario?>
    suspend fun actualizarUsuario(usuario: Usuario)

    suspend fun registrar(nombre: String, nombreUsuario: String, contrasena: String): Usuario?

    fun obtenerUsuarioConMascotas(id: Int): Flow<UsuarioConMascotas?>
}
