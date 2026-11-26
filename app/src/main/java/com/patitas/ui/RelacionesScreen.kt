package com.patitas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.patitas.R

// Pantalla de demostración de las relaciones de Room (equivale a RelacionesScreen de la Práctica 2),
// con las tablas USUARIO y MASCOTA. Se abre desde Perfil.
@Composable
fun RelacionesScreen(
    navController: NavController,
    viewModel: RelacionesViewModel = hiltViewModel()
) {
    val usuarios by viewModel.usuariosConMascotas.collectAsStateWithLifecycle()
    val mascotas by viewModel.mascotasConUsuario.collectAsStateWithLifecycle()

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BotonCircular(
                            icono = Icons.AutoMirrored.Filled.ArrowBack,
                            descripcion = stringResource(R.string.comun_volver),
                            onClick = { navController.popBackStack() }
                        )
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.relaciones_titulo),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.relaciones_subtitulo),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 1) UNO A MUCHOS: cada usuario con la lista de sus mascotas
            item { Titulo(stringResource(R.string.relaciones_uno_a_muchos)) }
            // Las llaves (key) llevan un prefijo para que no se repitan entre secciones
            items(usuarios, key = { "um-${it.usuario.idUsuario}" }) { item ->
                val nombres = item.mascotas.joinToString(", ") { it.nombre }
                TarjetaTexto(
                    titulo = item.usuario.nombre.ifBlank { item.usuario.nombreUsuario },
                    detalle = stringResource(
                        R.string.relaciones_mascotas,
                        nombres.ifEmpty { stringResource(R.string.relaciones_sin_mascotas) }
                    )
                )
            }

            // 2) MUCHOS A UNO: cada mascota con su dueño
            item { Titulo(stringResource(R.string.relaciones_muchos_a_uno)) }
            items(mascotas, key = { "mu-${it.mascota.idMascota}" }) { item ->
                TarjetaTexto(
                    titulo = item.mascota.nombre,
                    detalle = stringResource(
                        R.string.relaciones_dueno,
                        item.usuario.nombre.ifBlank { item.usuario.nombreUsuario }
                    )
                )
            }
        }
    }
}

@Composable
private fun Titulo(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(top = 12.dp)
    )
}

@Composable
private fun TarjetaTexto(titulo: String, detalle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = detalle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
