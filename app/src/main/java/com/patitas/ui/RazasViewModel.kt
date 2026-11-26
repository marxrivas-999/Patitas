package com.patitas.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patitas.domain.repository.RazaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Estado de la UI (igual que UserUiState de la Práctica 2)
data class RazasUiState(
    val isLoading: Boolean = false,
    val razas: List<String> = emptyList(),
    val hayError: Boolean = false      // el texto del error está en strings.xml, no se guarda aquí
)

// 2. Eventos de la UI (flujo ascendente)
sealed interface RazasUiEvent {
    data object Recargar : RazasUiEvent
}

@HiltViewModel
class RazasViewModel @Inject constructor(
    private val repository: RazaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RazasUiState())
    val uiState: StateFlow<RazasUiState> = _uiState.asStateFlow()

    init {
        onEvent(RazasUiEvent.Recargar)
    }

    fun onEvent(event: RazasUiEvent) {
        when (event) {
            is RazasUiEvent.Recargar -> cargarRazas()
        }
    }

    private fun cargarRazas() {
        if (_uiState.value.isLoading) return   // evita pedir lo mismo dos veces a la vez
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hayError = false) }
            repository.obtenerRazasPerro().fold(
                onSuccess = { lista ->
                    _uiState.update { it.copy(isLoading = false, razas = lista) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false, hayError = true) }
                }
            )
        }
    }
}
