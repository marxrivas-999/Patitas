package com.patitas.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.patitas.R
import com.patitas.data.AppScreens
import com.patitas.ui.theme.PatitasTheme

// Pantalla 2: Inicio de sesión (verifica el usuario en la tabla USUARIO)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: UsuarioViewModel = hiltViewModel()
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    val error by viewModel.errorLogin.collectAsStateWithLifecycle()

    LoginContenido(
        usuario = usuario,
        onUsuarioCambia = { usuario = it; viewModel.limpiarError() },
        contrasena = contrasena,
        onContrasenaCambia = { contrasena = it; viewModel.limpiarError() },
        hayError = error,
        onVolver = { navController.popBackStack() },
        onCrearCuenta = { navController.navigate(AppScreens.Registro.route) },
        onEntrar = {
            viewModel.iniciarSesion(usuario, contrasena) {
                navController.navigate(AppScreens.Inicio.route) {
                    popUpTo(AppScreens.Bienvenida.route) { inclusive = true }
                }
            }
        }
    )
}

@Composable
fun LoginContenido(
    usuario: String,
    onUsuarioCambia: (String) -> Unit,
    contrasena: String,
    onContrasenaCambia: (String) -> Unit,
    hayError: Boolean,
    onVolver: () -> Unit,
    onEntrar: () -> Unit,
    onCrearCuenta: () -> Unit = {}
) {
    var mostrarContrasena by remember { mutableStateOf(false) }
    val textoError: (@Composable () -> Unit)? =
        if (hayError) {
            { Text(stringResource(R.string.login_error)) }
        } else null

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
                descripcion = stringResource(R.string.login_volver),
                onClick = onVolver
            )

            Spacer(Modifier.height(20.dp))
            LogoPatitas(tamanoHuella = 40.dp)
            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.login_titulo),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.login_subtitulo),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            CampoPatitas(
                etiqueta = stringResource(R.string.login_usuario),
                valor = usuario,
                onValorCambia = onUsuarioCambia,
                placeholder = stringResource(R.string.login_usuario_hint),
                icono = Icons.Outlined.Person,
                teclado = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(Modifier.height(14.dp))

            // Campo de contraseña (con botón para mostrar u ocultar)
            Text(
                text = stringResource(R.string.login_contrasena),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(6.dp))
            androidx.compose.material3.OutlinedTextField(
                value = contrasena,
                onValueChange = onContrasenaCambia,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        stringResource(R.string.login_contrasena_hint),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                        Icon(
                            imageVector = if (mostrarContrasena) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = stringResource(
                                if (mostrarContrasena) R.string.login_ocultar else R.string.login_mostrar
                            )
                        )
                    }
                },
                isError = hayError,
                supportingText = textoError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (mostrarContrasena) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                shape = MaterialTheme.shapes.medium,
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onEntrar,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.login_boton),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            androidx.compose.material3.TextButton(
                onClick = onCrearCuenta,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.login_crear_cuenta),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.login_info_titulo),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.login_info),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.login_confianza),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    PatitasTheme {
        LoginContenido(
            usuario = "ana garcia",
            onUsuarioCambia = {},
            contrasena = "12345678",
            onContrasenaCambia = {},
            hayError = false,
            onVolver = {},
            onEntrar = {}
        )
    }
}
