package com.patitas.domain.repository

import com.patitas.domain.model.Usuario
import com.patitas.domain.model.UsuarioConMascotas
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    // Devuelve el usuario si el nombre y la contraseña son correctos; si no, null
    suspend fun iniciarSesion(nombreUsuario: String, contrasena: String): Usuario?
    fun obtenerUsuario(id: Int): Flow<Usuario?>
    suspend fun actualizarUsuario(usuario: Usuario)
    // Crea la cuenta y devuelve el usuario nuevo; null si ese nombre de usuario ya existe
    suspend fun registrar(nombre: String, nombreUsuario: String, contrasena: String): Usuario?
    // Relación 1 a N: el usuario junto con todas sus mascotas
    fun obtenerUsuarioConMascotas(id: Int): Flow<UsuarioConMascotas?>
}
