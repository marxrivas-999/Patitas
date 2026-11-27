package com.patitas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patitas.data.SesionUsuario
import com.patitas.domain.model.Usuario
import com.patitas.domain.model.UsuarioConMascotas
import com.patitas.domain.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
//los repositorios reciben su DAO
class UsuarioViewModel @Inject constructor(
    private val repository: UsuarioRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    val usuario: StateFlow<Usuario?> = sesion.idUsuario
        .flatMapLatest { id -> if (id == null) flowOf(null) else repository.obtenerUsuario(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val usuarioConMascotas: StateFlow<UsuarioConMascotas?> = sesion.idUsuario
        .flatMapLatest { id -> if (id == null) flowOf(null) else repository.obtenerUsuarioConMascotas(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _errorLogin = MutableStateFlow(false)
    val errorLogin: StateFlow<Boolean> = _errorLogin

    fun iniciarSesion(nombreUsuario: String, contrasena: String, alEntrar: () -> Unit) {
        viewModelScope.launch {
            val encontrado = repository.iniciarSesion(nombreUsuario.trim(), contrasena)
            if (encontrado != null) {
                _errorLogin.value = false
                sesion.iniciar(encontrado.idUsuario)
                alEntrar()
            } else {
                _errorLogin.value = true
            }
        }
    }

    fun limpiarError() { _errorLogin.value = false }

    private val _usuarioYaExiste = MutableStateFlow(false)
    val usuarioYaExiste: StateFlow<Boolean> = _usuarioYaExiste

    fun limpiarErrorRegistro() { _usuarioYaExiste.value = false }

    fun registrar(nombre: String, nombreUsuario: String, contrasena: String, alEntrar: () -> Unit) {
        viewModelScope.launch {
            val nuevo = repository.registrar(nombre, nombreUsuario, contrasena)
            if (nuevo != null) {
                _usuarioYaExiste.value = false
                sesion.iniciar(nuevo.idUsuario)
                alEntrar()
            } else {
                _usuarioYaExiste.value = true
            }
        }
    }

    fun cerrarSesion() = sesion.cerrar()

    fun cambiarNotificaciones(activadas: Boolean) {
        val actual = usuario.value ?: return
        viewModelScope.launch {
            repository.actualizarUsuario(actual.copy(recibirNotificaciones = activadas))
        }
    }

    fun cambiarFoto(ruta: String) {
        val actual = usuario.value ?: return
        viewModelScope.launch {
            repository.actualizarUsuario(actual.copy(foto = ruta))
        }
    }
}
