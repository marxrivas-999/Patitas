package com.patitas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.patitas.R
import com.patitas.data.AppScreens
import com.patitas.domain.model.Mascota
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

// Pantalla 4: Agregar mascota (con idMascota > 0 se usa para editar la ficha)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormMascotaScreen(
    navController: NavController,
    idMascota: Int,
    viewModel: MascotaViewModel = hiltViewModel(),
    razasViewModel: RazasViewModel = hiltViewModel()
) {
    val editando = idMascota != 0
    // Razas de perro que llegan de la API (Retrofit); si falla se usa la lista local
    val estadoRazas by razasViewModel.uiState.collectAsStateWithLifecycle()
    val existente by remember(idMascota) { viewModel.obtenerMascota(idMascota) }
        .collectAsStateWithLifecycle(initialValue = null)

    val especies = listOf(
        stringResource(R.string.especie_perro),
        stringResource(R.string.especie_gato),
        stringResource(R.string.especie_otro)
    )

    var cargado by rememberSaveable { mutableStateOf(!editando) }
    var foto by rememberSaveable { mutableStateOf("") }
    var nombre by rememberSaveable { mutableStateOf("") }
    var especie by rememberSaveable { mutableStateOf(especies[0]) }
    var raza by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }          // aaaa-mm-dd
    var peso by rememberSaveable { mutableStateOf("") }
    var alergias by rememberSaveable { mutableStateOf("") }
    var notas by rememberSaveable { mutableStateOf("") }
    var errorNombre by rememberSaveable { mutableStateOf(false) }
    var errorPeso by rememberSaveable { mutableStateOf(false) }
    var menuRaza by remember { mutableStateOf(false) }
    var mostrarFecha by remember { mutableStateOf(false) }
    var mostrarEliminar by remember { mutableStateOf(false) }

    // Al editar, se llenan los campos con los datos guardados (una sola vez)
    LaunchedEffect(existente) {
        val m = existente
        if (m != null && !cargado) {
            foto = m.foto
            nombre = m.nombre
            especie = m.especie.ifBlank { especies[0] }
            raza = m.raza
            fecha = m.fechaNacimiento
            peso = m.pesoKg?.let { formatearPeso(it) } ?: ""
            alergias = m.alergias
            notas = m.notas
            cargado = true
        }
    }

    val abrirGaleria = rememberSelectorFoto { ruta -> foto = ruta }

    val razasPerroLocales = stringArrayResource(R.array.razas_perro)
    val razas = when (especie) {
        // Perro: las razas locales primero y después las que trae la API (sin repetir)
        especies[0] -> remember(estadoRazas.razas) {
            combinarRazas(razasPerroLocales.toList(), estadoRazas.razas)
        }
        especies[1] -> stringArrayResource(R.array.razas_gato).toList()
        else -> stringArrayResource(R.array.razas_otro).toList()
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Flecha de regreso y nombre de la app
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
                text = stringResource(
                    if (editando) R.string.form_titulo_editar else R.string.form_titulo_agregar
                ),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.form_subtitulo),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            // Botón circular para subir la foto
            Card(
                modifier = Modifier.fillMaxWidth().clickable(onClick = abrirGaleria),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ImagenDesdeRuta(
                        ruta = foto,
                        descripcion = stringResource(R.string.form_foto_descripcion),
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = stringResource(R.string.form_subir_foto),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            text = stringResource(
                                if (foto.isBlank()) R.string.form_subir_foto else R.string.form_cambiar_foto
                            ),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = stringResource(R.string.form_foto_texto),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            CampoPatitas(
                etiqueta = stringResource(R.string.form_nombre),
                valor = nombre,
                onValorCambia = {
                    if (it.length <= 20) nombre = it
                    errorNombre = false
                },
                placeholder = stringResource(R.string.form_nombre_hint),
                error = if (errorNombre) stringResource(R.string.form_nombre_error) else null
            )

            Spacer(Modifier.height(14.dp))

            // Chips de selección: especie
            Text(
                text = stringResource(R.string.form_especie),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                especies.forEach { opcion ->
                    FilterChip(
                        selected = especie == opcion,
                        onClick = {
                            if (especie != opcion) {
                                especie = opcion
                                raza = ""      // las razas dependen de la especie
                            }
                        },
                        label = { Text(opcion, style = MaterialTheme.typography.labelMedium) },
                        shape = CircleShape,
                        border = null,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Selector desplegable: raza
            Box {
                CampoPatitas(
                    etiqueta = stringResource(R.string.form_raza),
                    valor = raza,
                    onValorCambia = {},
                    placeholder = stringResource(R.string.form_raza_hint),
                    icono = Icons.Filled.KeyboardArrowDown,
                    soloLectura = true,
                    onClick = { menuRaza = true }
                )
                DropdownMenu(expanded = menuRaza, onDismissRequest = { menuRaza = false }) {
                    // Estado de la consulta a la API (solo aplica a perros)
                    if (especie == especies[0]) {
                        when {
                            estadoRazas.isLoading -> DropdownMenuItem(
                                text = { Text(stringResource(R.string.form_razas_cargando)) },
                                onClick = {},
                                enabled = false
                            )
                            estadoRazas.hayError -> DropdownMenuItem(
                                text = { Text(stringResource(R.string.form_razas_reintentar)) },
                                onClick = { razasViewModel.onEvent(RazasUiEvent.Recargar) }
                            )
                        }
                    }
                    razas.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                raza = opcion
                                menuRaza = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Selector de fecha: fecha de nacimiento
            CampoPatitas(
                etiqueta = stringResource(R.string.form_fecha),
                valor = fechaParaMostrar(fecha),
                onValorCambia = {},
                placeholder = stringResource(R.string.form_fecha_hint),
                icono = Icons.Outlined.CalendarToday,
                soloLectura = true,
                onClick = { mostrarFecha = true }
            )

            Spacer(Modifier.height(14.dp))

            // Campo numérico: peso
            CampoPatitas(
                etiqueta = stringResource(R.string.form_peso),
                valor = peso,
                onValorCambia = {
                    peso = it
                    errorPeso = false
                },
                placeholder = stringResource(R.string.form_peso_hint),
                icono = Icons.Outlined.MonitorWeight,
                teclado = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                error = if (errorPeso) stringResource(R.string.form_peso_error) else null
            )

            // Al editar la ficha también se capturan las alergias y la sección "Acerca de"
            if (editando) {
                Spacer(Modifier.height(14.dp))
                CampoPatitas(
                    etiqueta = stringResource(R.string.form_alergias),
                    valor = alergias,
                    onValorCambia = { alergias = it },
                    placeholder = stringResource(R.string.form_alergias_hint)
                )
                Spacer(Modifier.height(14.dp))
                CampoPatitas(
                    etiqueta = stringResource(R.string.form_notas),
                    valor = notas,
                    onValorCambia = { notas = it },
                    placeholder = stringResource(R.string.form_notas_hint),
                    unaLinea = false
                )
            }

            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.form_nota_pie),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val pesoNumero = peso.trim().replace(',', '.').toFloatOrNull()
                    when {
                        nombre.isBlank() -> errorNombre = true
                        peso.isNotBlank() && pesoNumero == null -> errorPeso = true
                        else -> {
                            viewModel.guardarMascota(
                                (existente ?: Mascota()).copy(
                                    nombre = nombre.trim(),
                                    especie = especie,
                                    raza = raza,
                                    fechaNacimiento = fecha,
                                    pesoKg = pesoNumero,
                                    foto = foto,
                                    alergias = alergias.trim(),
                                    notas = notas.trim()
                                )
                            )
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(
                        if (editando) R.string.form_guardar_cambios else R.string.form_guardar
                    ),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.comun_cancelar),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (editando) {
                TextButton(
                    onClick = { mostrarEliminar = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.form_eliminar),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    // Calendario para elegir la fecha de nacimiento (no permite fechas futuras)
    if (mostrarFecha) {
        val milisIniciales = runCatching {
            LocalDate.parse(fecha).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        }.getOrNull()
        val estado = rememberDatePickerState(
            initialSelectedDateMillis = milisIniciales,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                    utcTimeMillis <= System.currentTimeMillis()
            }
        )
        DatePickerDialog(
            onDismissRequest = { mostrarFecha = false },
            confirmButton = {
                TextButton(onClick = {
                    estado.selectedDateMillis?.let {
                        fecha = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate().toString()
                    }
                    mostrarFecha = false
                }) { Text(stringResource(R.string.comun_aceptar)) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarFecha = false }) {
                    Text(stringResource(R.string.comun_cancelar))
                }
            }
        ) {
            DatePicker(state = estado)
        }
    }

    // Confirmación antes de eliminar (prevención de errores)
    if (mostrarEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarEliminar = false },
            title = { Text(stringResource(R.string.form_eliminar_titulo, nombre)) },
            text = { Text(stringResource(R.string.form_eliminar_texto)) },
            confirmButton = {
                TextButton(onClick = {
                    mostrarEliminar = false
                    existente?.let { viewModel.eliminarMascota(it) }
                    navController.popBackStack(AppScreens.Inicio.route, inclusive = false)
                }) {
                    Text(
                        text = stringResource(R.string.form_eliminar),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarEliminar = false }) {
                    Text(stringResource(R.string.comun_cancelar))
                }
            }
        )
    }
}

// Une las razas locales (en español) con las de la API sin repetir nombres (sin distinguir mayúsculas)
private fun combinarRazas(locales: List<String>, remotas: List<String>): List<String> {
    val conocidas = locales.map { it.lowercase() }.toSet()
    val extra = remotas.filter { it.lowercase() !in conocidas }
    // "Mestizo" se queda al final de la lista local, por eso las de la API van antes
    val mestizo = locales.lastOrNull()
    return if (extra.isEmpty() || mestizo == null) locales + extra
    else locales.dropLast(1) + extra + mestizo
}
