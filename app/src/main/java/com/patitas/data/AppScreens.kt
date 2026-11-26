package com.patitas.data

sealed class AppScreens(val route: String) {
    object Bienvenida : AppScreens("Bienvenida")
    object Login : AppScreens("Login")
    object Registro : AppScreens("Registro")
    object Inicio : AppScreens("Inicio")
    object Perfil : AppScreens("Perfil")
    object Sensores : AppScreens("Sensores")
    object Relaciones : AppScreens("Relaciones")
    object Privacidad : AppScreens("Privacidad")

    object FormMascota : AppScreens("FormMascota/{id}") {
        fun conId(id: Int) = "FormMascota/$id"
    }

    object PerfilMascota : AppScreens("PerfilMascota/{id}") {
        fun conId(id: Int) = "PerfilMascota/$id"
    }
}
