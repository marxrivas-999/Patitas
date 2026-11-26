package com.patitas.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.patitas.R
import com.patitas.data.AppScreens

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel = hiltViewModel()
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var usuario by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }
    var errorUsuario by rememberSaveable { mutableStateOf(false) }
    var errorContrasena by rememberSaveable { mutableStateOf(false) }
    var errorConfirmar by rememberSaveable { mutableStateOf(false) }
    var acepta by rememberSaveable { mutableStateOf(false) }
    var errorAcepta by rememberSaveable { mutableStateOf(false) }
    var mostrar by remember { mutableStateOf(false) }
    val usuarioYaExiste by viewModel.usuarioYaExiste.collectAsStateWithLifecycle()

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            BotonCircular(
                icono = Icons.AutoMirrored.Filled.ArrowBack,
                descripcion = stringResource(R.string.comun_volver),
                onClick = {
                    viewModel.limpiarErrorRegistro()
                    navController.popBackStack()
                }
            )

            Spacer(Modifier.height(20.dp))
            LogoPatitas(tamanoHuella = 40.dp)
            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.registro_titulo),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.registro_subtitulo),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            CampoPatitas(
                etiqueta = stringResource(R.string.registro_nombre),
                valor = nombre,
                onValorCambia = { nombre = it },
                placeholder = stringResource(R.string.registro_nombre_hint),
                icono = Icons.Outlined.Person
            )

            Spacer(Modifier.height(14.dp))

            CampoPatitas(
                etiqueta = stringResource(R.string.login_usuario),
                valor = usuario,
                onValorCambia = {
                    usuario = it
                    errorUsuario = false
                    viewModel.limpiarErrorRegistro()
                },
                placeholder = stringResource(R.string.login_usuario_hint),
                icono = Icons.Outlined.Person,
                error = when {
                    errorUsuario -> stringResource(R.string.registro_usuario_error)
                    usuarioYaExiste -> stringResource(R.string.registro_usuario_existe)
                    else -> null
                }
            )

            Spacer(Modifier.height(14.dp))

            CampoContrasena(
                etiqueta = stringResource(R.string.login_contrasena),
                hint = stringResource(R.string.login_contrasena_hint),
                valor = contrasena,
                onCambia = { contrasena = it; errorContrasena = false },
                mostrar = mostrar,
                onAlternar = { mostrar = !mostrar },
                error = if (errorContrasena) stringResource(R.string.registro_contrasena_error) else null
            )

            Spacer(Modifier.height(14.dp))

            CampoContrasena(
                etiqueta = stringResource(R.string.registro_confirmar),
                hint = stringResource(R.string.registro_confirmar_hint),
                valor = confirmar,
                onCambia = { confirmar = it; errorConfirmar = false },
                mostrar = mostrar,
                onAlternar = { mostrar = !mostrar },
                error = if (errorConfirmar) stringResource(R.string.registro_confirmar_error) else null
            )

            Spacer(Modifier.height(14.dp))


            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = acepta,
                    onCheckedChange = { acepta = it; errorAcepta = false }
                )
                Text(
                    text = stringResource(R.string.registro_acepto),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            TextButton(onClick = { navController.navigate(AppScreens.Privacidad.route) }) {
                Text(
                    text = stringResource(R.string.registro_leer_aviso),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (errorAcepta) {
                Text(
                    text = stringResource(R.string.registro_acepto_error),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    errorUsuario = usuario.isBlank()
                    errorContrasena = contrasena.length < 8
                    errorConfirmar = confirmar != contrasena
                    errorAcepta = !acepta
                    if (!errorUsuario && !errorContrasena && !errorConfirmar && acepta) {
                        viewModel.registrar(nombre.trim(), usuario.trim(), contrasena) {
                            // Cuenta creada y sesión iniciada: se entra directo a Inicio
                            navController.navigate(AppScreens.Inicio.route) {
                                popUpTo(AppScreens.Bienvenida.route) { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.registro_boton),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CampoContrasena(
    etiqueta: String,
    hint: String,
    valor: String,
    onCambia: (String) -> Unit,
    mostrar: Boolean,
    onAlternar: () -> Unit,
    error: String?
) {
    Text(
        text = etiqueta,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(Modifier.height(6.dp))
    OutlinedTextField(
        value = valor,
        onValueChange = onCambia,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(hint, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = onAlternar) {
                Icon(
                    imageVector = if (mostrar) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = stringResource(
                        if (mostrar) R.string.login_ocultar else R.string.login_mostrar
                    )
                )
            }
        },
        isError = error != null,
        supportingText = if (error != null) {
            { Text(error) }
        } else null,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (mostrar) VisualTransformation.None else PasswordVisualTransformation(),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}
