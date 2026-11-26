package com.patitas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patitas.data.SesionUsuario
import com.patitas.domain.model.Mascota
import com.patitas.domain.repository.MascotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MascotaViewModel @Inject constructor(
    private val repository: MascotaRepository,
    private val sesion: SesionUsuario
) : ViewModel() {

    // Mascotas del usuario que tiene la sesión iniciada
    val mascotas: StateFlow<List<Mascota>> = sesion.idUsuario
        .flatMapLatest { id -> if (id == null) flowOf(emptyList()) else repository.obtenerMascotas(id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun obtenerMascota(id: Int): Flow<Mascota?> = repository.obtenerMascota(id)

    // Si la mascota es nueva (id = 0) se inserta; si ya existe, se actualiza
    fun guardarMascota(mascota: Mascota) {
        val idUsuario = sesion.idUsuario.value ?: return
        viewModelScope.launch {
            if (mascota.idMascota == 0) {
                repository.insertarMascota(mascota.copy(idUsuario = idUsuario))
            } else {
                repository.actualizarMascota(mascota)
            }
        }
    }

    fun eliminarMascota(mascota: Mascota) {
        viewModelScope.launch { repository.deleteMascota(mascota) }
    }

    fun alternarFavorita(mascota: Mascota) {
        viewModelScope.launch {
            repository.actualizarMascota(mascota.copy(esFavorita = !mascota.esFavorita))
        }
    }
}
