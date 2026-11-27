package com.patitas.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.patitas.data.AppScreens

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.Bienvenida.route
    ) {
        composable(route = AppScreens.Bienvenida.route) {
            BienvenidaScreen(navController)
        }
        composable(route = AppScreens.Login.route) {
            LoginScreen(navController)
        }
        composable(route = AppScreens.Registro.route) {
            RegistroScreen(navController)
        }
        composable(route = AppScreens.Inicio.route) {
            InicioScreen(navController)
        }
        composable(route = AppScreens.Perfil.route) {
            PerfilScreen(navController)
        }
        composable(route = AppScreens.Sensores.route) {
            SensoresScreen(navController)
        }
        composable(route = AppScreens.Relaciones.route) {
            RelacionesScreen(navController)
        }
        composable(route = AppScreens.Privacidad.route) {
            PrivacidadScreen(navController)
        }
        composable(
            route = AppScreens.FormMascota.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            FormMascotaScreen(navController, idMascota = entrada.arguments?.getInt("id") ?: 0)
        }
        composable(
            route = AppScreens.PerfilMascota.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            PerfilMascotaScreen(navController, idMascota = entrada.arguments?.getInt("id") ?: 0)
        }
    }
}

//16: •	NavHost: contenedor que muestra la pantalla que corresponde a la ruta actual.
// Recibe un NavController y una ruta inicial.
//•	composable(route) { }: función de NavGraphBuilder que registra un destino: asocia una ruta (texto)
// con la pantalla que se dibuja.
//En tu proyecto: ui/AppNavegacion.kt: rememberNavController(), NavHost(startDestination = Bienvenida)
// y 10 llamadas a composable(...). Dos reciben argumento con navArgument("id"). Las rutas están en
// data/AppScreens.kt.