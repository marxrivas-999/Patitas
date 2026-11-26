package com.patitas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patitas.domain.model.MascotaConUsuario
import com.patitas.domain.model.UsuarioConMascotas
import com.patitas.domain.repository.RelacionesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RelacionesViewModel @Inject constructor(
    repository: RelacionesRepository
) : ViewModel() {

    val usuariosConMascotas: StateFlow<List<UsuarioConMascotas>> = repository.usuariosConMascotas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val mascotasConUsuario: StateFlow<List<MascotaConUsuario>> = repository.mascotasConUsuario()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
