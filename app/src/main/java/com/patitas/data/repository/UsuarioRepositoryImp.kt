package com.patitas.data.repository

import com.patitas.data.Seguridad
import com.patitas.data.database.dao.UsuarioDao
import com.patitas.data.database.entity.UsuarioEntity
import com.patitas.data.mapper.toDomain
import com.patitas.data.mapper.toEntity
import com.patitas.domain.model.Usuario
import com.patitas.domain.model.UsuarioConMascotas
import com.patitas.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsuarioRepositoryImp @Inject constructor(
    private val usuarioDao: UsuarioDao
) : UsuarioRepository {
    //11:Una función que puede pausarse y reanudarse sin bloquear el hilo principal.
    // Sirve para tareas lentas (base de datos, internet) sin congelar la pantalla. Solo se
    // puede llamar desde una corrutina o desde otra función suspend.
    //DAO: suspend fun insertarMascota(...), actualizarMascota, eliminarMascota.
    //	Repositorios: iniciarSesion, registrar, obtenerRazasPerro.
    //	Retrofit: RazaApiService.
    //	Los ViewModels las llaman dentro de viewModelScope.launch { } (la corrutina).

    override suspend fun iniciarSesion(nombreUsuario: String, contrasena: String): Usuario? {
        val usuario = usuarioDao.buscarPorNombreUsuario(nombreUsuario) ?: return null
        // La contraseña nunca se guarda en texto plano: se compara su hash
        return if (usuario.contrasenaHash == Seguridad.hash(contrasena)) usuario.toDomain() else null
    }

    override suspend fun registrar(nombre: String, nombreUsuario: String, contrasena: String): Usuario? {
        // El nombre de usuario es único (índice único en la tabla USUARIO)
        if (usuarioDao.buscarPorNombreUsuario(nombreUsuario) != null) return null
        usuarioDao.insertarUsuario(
            UsuarioEntity(
                nombreUsuario = nombreUsuario,
                contrasenaHash = Seguridad.hash(contrasena),   // nunca se guarda en texto plano
                nombre = nombre
            )
        )
        return usuarioDao.buscarPorNombreUsuario(nombreUsuario)?.toDomain()
    }

    override fun obtenerUsuario(id: Int): Flow<Usuario?> {
        return usuarioDao.getUsuario(id).map { it?.toDomain() }
    }

    override suspend fun actualizarUsuario(usuario: Usuario) {
        usuarioDao.actualizarUsuario(usuario.toEntity())
    }

    override fun obtenerUsuarioConMascotas(id: Int): Flow<UsuarioConMascotas?> {
        return usuarioDao.obtenerUsuarioConMascotas(id).map { it?.toDomain() }
    }
}
