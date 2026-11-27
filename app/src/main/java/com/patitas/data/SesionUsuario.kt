package com.patitas.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton
//Reutilización de instancia desde el data base
@Singleton
class SesionUsuario @Inject constructor() {
    private val _idUsuario = MutableStateFlow<Int?>(null)
    val idUsuario: StateFlow<Int?> = _idUsuario

    fun iniciar(id: Int) { _idUsuario.value = id }
    fun cerrar() { _idUsuario.value = null }
}
