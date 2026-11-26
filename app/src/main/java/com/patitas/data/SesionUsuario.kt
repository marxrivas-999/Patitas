package com.patitas.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

// Guarda qué usuario inició sesión mientras la aplicación está abierta
@Singleton
class SesionUsuario @Inject constructor() {
    private val _idUsuario = MutableStateFlow<Int?>(null)
    val idUsuario: StateFlow<Int?> = _idUsuario

    fun iniciar(id: Int) { _idUsuario.value = id }
    fun cerrar() { _idUsuario.value = null }
}
