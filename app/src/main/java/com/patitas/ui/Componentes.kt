package com.patitas.ui

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.patitas.R
import com.patitas.data.AppScreens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

// ---------- Botón circular (flecha de regreso, campana, engrane, corazón) ----------
@Composable
fun BotonCircular(
    icono: ImageVector,
    descripcion: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier.size(44.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(imageVector = icono, contentDescription = descripcion)
    }
}

// ---------- Barra de navegación inferior (Inicio, Mascotas, Historial, Perfil) ----------
enum class DestinoBarra { Inicio, Mascotas, Historial, Perfil }

@Composable
fun BarraInferior(
    navController: NavController,
    seleccionado: DestinoBarra,
    viewModel: MascotaViewModel = hiltViewModel()
) {
    val mascotas by viewModel.mascotas.collectAsStateWithLifecycle()
    val context = LocalContext.current

    fun irA(ruta: String) {
        navController.navigate(ruta) {
            popUpTo(AppScreens.Inicio.route)
            launchSingleTop = true
        }
    }

    val colores = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.onBackground,
        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    NavigationBar(containerColor = MaterialTheme.colorScheme.background, tonalElevation = 0.dp) {
        NavigationBarItem(
            selected = seleccionado == DestinoBarra.Inicio,
            onClick = { irA(AppScreens.Inicio.route) },
            icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_inicio)) },
            colors = colores
        )
        NavigationBarItem(
            selected = seleccionado == DestinoBarra.Mascotas,
            onClick = {
                // Abre la ficha de la primera mascota (las favoritas salen primero) o el formulario si no hay ninguna
                val primera = mascotas.firstOrNull()
                irA(
                    if (primera != null) AppScreens.PerfilMascota.conId(primera.idMascota)
                    else AppScreens.FormMascota.conId(0)
                )
            },
            icon = { Icon(Icons.Outlined.Pets, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_mascotas)) },
            colors = colores
        )
        NavigationBarItem(
            selected = seleccionado == DestinoBarra.Historial,
            onClick = { context.avisoProximamente() },   // pantalla de la tabla CUIDADO (siguiente revisión)
            icon = { Icon(Icons.AutoMirrored.Outlined.Assignment, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_historial)) },
            colors = colores
        )
        NavigationBarItem(
            selected = seleccionado == DestinoBarra.Perfil,
            onClick = { irA(AppScreens.Perfil.route) },
            icon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            label = { Text(stringResource(R.string.nav_perfil)) },
            colors = colores
        )
    }
}

// ---------- Campo de texto con etiqueta arriba (como en Figma) ----------
@Composable
fun CampoPatitas(
    etiqueta: String,
    valor: String,
    onValorCambia: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    soloLectura: Boolean = false,
    onClick: (() -> Unit)? = null,
    teclado: KeyboardOptions = KeyboardOptions.Default,
    error: String? = null,
    unaLinea: Boolean = true
) {
    val iconoInicial: (@Composable () -> Unit)? =
        if (icono != null) {
            { Icon(imageVector = icono, contentDescription = null) }
        } else null
    val textoError: (@Composable () -> Unit)? =
        if (error != null) {
            { Text(error) }
        } else null

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(6.dp))
        Box {
            OutlinedTextField(
                value = valor,
                onValueChange = onValorCambia,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                leadingIcon = iconoInicial,
                readOnly = soloLectura,
                isError = error != null,
                supportingText = textoError,
                singleLine = unaLinea,
                minLines = if (unaLinea) 1 else 3,
                keyboardOptions = teclado,
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            if (onClick != null) {
                // Capa transparente: el campo (fecha, raza) se comporta como un botón
                Box(Modifier.matchParentSize().clickable(onClick = onClick))
            }
        }
    }
}

// ---------- Fotografías guardadas en el almacenamiento de la app ----------
@Composable
fun ImagenDesdeRuta(
    ruta: String,
    descripcion: String?,
    modifier: Modifier = Modifier,
    contenidoVacio: @Composable () -> Unit
) {
    val imagen by produceState<ImageBitmap?>(initialValue = null, ruta) {
        value = if (ruta.isBlank()) null else withContext(Dispatchers.IO) { decodificar(ruta) }
    }
    val cargada = imagen
    if (cargada != null) {
        Image(
            bitmap = cargada,
            contentDescription = descripcion,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(modifier, contentAlignment = Alignment.Center) { contenidoVacio() }
    }
}

private fun decodificar(ruta: String): ImageBitmap? = try {
    ImageDecoder.decodeBitmap(ImageDecoder.createSource(File(ruta))) { decoder, info, _ ->
        // Reduce fotos muy grandes para ahorrar memoria
        var reduccion = 1
        while (info.size.width / reduccion > 1200) reduccion *= 2
        decoder.setTargetSampleSize(reduccion)
    }.asImageBitmap()
} catch (e: Exception) {
    null
}

// Copia la foto elegida a la carpeta privada de la app y regresa su ruta
private fun guardarFoto(context: Context, uri: Uri): String? = try {
    val carpeta = File(context.filesDir, "fotos").apply { mkdirs() }
    val archivo = File(carpeta, "${UUID.randomUUID()}.jpg")
    context.contentResolver.openInputStream(uri)?.use { entrada ->
        archivo.outputStream().use { salida -> entrada.copyTo(salida) }
    }
    archivo.absolutePath
} catch (e: Exception) {
    null
}

// Devuelve una función que abre la galería; al elegir una foto llama a alElegir(ruta)
@Composable
fun rememberSelectorFoto(alElegir: (String) -> Unit): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val ruta = withContext(Dispatchers.IO) { guardarFoto(context, uri) }
                if (ruta != null) alElegir(ruta)
            }
        }
    }
    return {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

// ---------- Utilidades ----------
fun Context.avisoProximamente() {
    Toast.makeText(this, getString(R.string.comun_proximamente), Toast.LENGTH_SHORT).show()
}

// Edad a partir de la fecha de nacimiento (aaaa-mm-dd); texto vacío si no hay fecha
fun edadTexto(context: Context, fechaNacimiento: String): String {
    val nacimiento = runCatching { LocalDate.parse(fechaNacimiento) }.getOrNull() ?: return ""
    val periodo = Period.between(nacimiento, LocalDate.now())
    if (periodo.isNegative) return ""
    val recursos = context.resources
    return when {
        periodo.years >= 1 -> recursos.getQuantityString(R.plurals.edad_anios, periodo.years, periodo.years)
        periodo.months >= 1 -> recursos.getQuantityString(R.plurals.edad_meses, periodo.months, periodo.months)
        else -> context.getString(R.string.edad_recien)
    }
}

fun fechaParaMostrar(fechaIso: String): String =
    runCatching { LocalDate.parse(fechaIso).format(DateTimeFormatter.ofPattern("dd / MM / yyyy")) }
        .getOrDefault("")

fun formatearPeso(peso: Float): String =
    String.format(Locale.forLanguageTag("es-ES"), "%.1f", peso).removeSuffix(",0")
