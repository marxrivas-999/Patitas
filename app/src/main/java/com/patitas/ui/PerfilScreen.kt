package com.patitas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.patitas.R
import com.patitas.data.AppScreens

// Pantalla 8: Perfil y configuración (datos de la tabla USUARIO)
@Composable
fun PerfilScreen(
    navController: NavController,
    viewModel: UsuarioViewModel = hiltViewModel()
) {
    val usuario by viewModel.usuario.collectAsStateWithLifecycle()
    val usuarioConMascotas by viewModel.usuarioConMascotas.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Resumen de la relación 1 a N (USUARIO -> MASCOTA): "3 mascotas · 1 favorita"
    val resumenMascotas = usuarioConMascotas?.let { datos ->
        val total = datos.mascotas.size
        val favoritas = datos.mascotas.count { it.esFavorita }
        val textoTotal = context.resources.getQuantityString(R.plurals.perfil_total_mascotas, total, total)
        if (favoritas > 0) {
            val textoFavoritas = context.resources.getQuantityString(R.plurals.perfil_total_favoritas, favoritas, favoritas)
            context.getString(R.string.perfil_mascotas_resumen, textoTotal, textoFavoritas)
        } else {
            textoTotal
        }
    }
    val abrirGaleria = rememberSelectorFoto { ruta -> viewModel.cambiarFoto(ruta) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BarraInferior(navController, DestinoBarra.Perfil) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título y engrane
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.perfil_titulo),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                BotonCircular(
                    icono = Icons.Outlined.Settings,
                    descripcion = stringResource(R.string.perfil_configuracion_boton),
                    onClick = { context.avisoProximamente() }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Fotografía de perfil (toca para cambiarla) y nombre
            val u = usuario
            ImagenDesdeRuta(
                ruta = u?.foto ?: "",
                descripcion = stringResource(R.string.perfil_foto_descripcion),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable(onClick = abrirGaleria)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = stringResource(R.string.perfil_foto_descripcion),
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = u?.let { it.nombre.ifBlank { it.nombreUsuario } } ?: "",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(20.dp))

            // Lista de opciones
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    OpcionPerfil(
                        Icons.Outlined.Pets,
                        stringResource(R.string.perfil_mis_mascotas),
                        detalle = resumenMascotas
                    ) {
                        navController.navigate(AppScreens.Inicio.route) {
                            popUpTo(AppScreens.Inicio.route)
                            launchSingleTop = true
                        }
                    }
                    OpcionPerfil(Icons.Outlined.Notifications, stringResource(R.string.perfil_notificaciones)) {
                        context.avisoProximamente()
                    }
                    OpcionPerfil(Icons.Outlined.Settings, stringResource(R.string.perfil_configuracion)) {
                        context.avisoProximamente()
                    }
                    OpcionPerfil(Icons.Outlined.Link, stringResource(R.string.perfil_relaciones)) {
                        navController.navigate(AppScreens.Relaciones.route)
                    }
                    OpcionPerfil(Icons.Outlined.Sensors, stringResource(R.string.perfil_sensores)) {
                        navController.navigate(AppScreens.Sensores.route)
                    }
                    OpcionPerfil(Icons.AutoMirrored.Outlined.HelpOutline, stringResource(R.string.perfil_ayuda)) {
                        context.avisoProximamente()
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Configuración: interruptor de notificaciones (campo recibir_notificaciones)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.perfil_seccion_config),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.perfil_recibir),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = stringResource(R.string.perfil_recibir_texto),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Switch(
                            checked = u?.recibirNotificaciones ?: false,
                            onCheckedChange = { viewModel.cambiarNotificaciones(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Cerrar sesión
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                        viewModel.cerrarSesion()
                        navController.navigate(AppScreens.Login.route) {
                            popUpTo(0)
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.perfil_cerrar_sesion),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.perfil_pie),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun OpcionPerfil(
    icono: ImageVector,
    texto: String,
    detalle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = texto,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (detalle != null) {
                Text(
                    text = detalle,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
