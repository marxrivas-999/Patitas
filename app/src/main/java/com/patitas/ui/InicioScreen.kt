package com.patitas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.patitas.R
import com.patitas.data.AppScreens
import com.patitas.domain.model.Mascota
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Pantalla 3: Inicio
@Composable
fun InicioScreen(
    navController: NavController,
    viewModel: MascotaViewModel = hiltViewModel()
) {
    val mascotas by viewModel.mascotas.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val local = Locale.forLanguageTag("es-MX")
    val fecha = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", local))
        .uppercase(local)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BarraInferior(navController, DestinoBarra.Inicio) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Saludo y campana de notificaciones
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = fecha,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.inicio_saludo),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BotonCircular(
                        icono = Icons.Outlined.Notifications,
                        descripcion = stringResource(R.string.inicio_notificaciones),
                        onClick = { context.avisoProximamente() }   // tabla NOTIFICACION (siguiente revisión)
                    )
                    Text(
                        text = stringResource(R.string.inicio_notificaciones),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Mis mascotas (datos de la tabla MASCOTA)
            Text(
                text = stringResource(R.string.inicio_mis_mascotas),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(12.dp))

            if (mascotas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        text = stringResource(R.string.inicio_sin_mascotas),
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                mascotas.forEach { mascota ->
                    TarjetaMascota(
                        mascota = mascota,
                        onClick = {
                            navController.navigate(AppScreens.PerfilMascota.conId(mascota.idMascota))
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            Spacer(Modifier.height(4.dp))
            OutlinedButton(
                onClick = { navController.navigate(AppScreens.FormMascota.conId(0)) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.inicio_agregar_mascota),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Próximos cuidados: tarjeta de muestra, se conectará con la tabla CUIDADO
            if (mascotas.isNotEmpty()) {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.inicio_proximos),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconoCirculo(Icons.Outlined.Vaccines)
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = stringResource(R.string.inicio_cuidado_titulo),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = stringResource(R.string.inicio_cuidado_detalle, mascotas.first().nombre),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.inicio_cuidado_texto),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(10.dp))
                        EtiquetaEstado(stringResource(R.string.estado_pendiente))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Accesos rápidos
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AccesoRapido(
                    icono = Icons.Outlined.AddCircleOutline,
                    texto = stringResource(R.string.inicio_registrar_cuidado),
                    modifier = Modifier.weight(1f),
                    onClick = { context.avisoProximamente() }
                )
                AccesoRapido(
                    icono = Icons.AutoMirrored.Outlined.Assignment,
                    texto = stringResource(R.string.inicio_ver_historial),
                    modifier = Modifier.weight(1f),
                    onClick = { context.avisoProximamente() }
                )
            }

            Spacer(Modifier.height(12.dp))

            // Tarjeta de consejos
            Card(
                modifier = Modifier.fillMaxWidth().clickable { context.avisoProximamente() },
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconoCirculo(Icons.AutoMirrored.Outlined.MenuBook)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.inicio_consejos_titulo),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = stringResource(R.string.inicio_consejos_texto),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

// Tarjeta de una mascota: foto, nombre, especie y edad, raza y enlace "Ver ficha"
@Composable
private fun TarjetaMascota(mascota: Mascota, onClick: () -> Unit) {
    val context = LocalContext.current
    val edad = edadTexto(context, mascota.fechaNacimiento)
    val especieYEdad = listOf(mascota.especie, edad).filter { it.isNotBlank() }.joinToString(" · ")

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.height(124.dp).padding(8.dp)) {
            ImagenDesdeRuta(
                ruta = mascota.foto,
                descripcion = mascota.nombre,
                modifier = Modifier
                    .width(116.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Pets,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = mascota.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = especieYEdad,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (mascota.raza.isNotBlank()) {
                    Text(
                        text = mascota.raza,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.inicio_ver_ficha),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Outlined.ArrowOutward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun IconoCirculo(icono: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun EtiquetaEstado(texto: String) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun AccesoRapido(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    texto: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(6.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}
