package com.patitas

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.patitas.ui.AppNavegacion
import com.patitas.ui.theme.PatitasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            PatitasTheme {
                AppNavegacion()
            }
        }
    }
}









//15: Le dice a Hilt que esa clase de Android puede recibir inyección de dependencias.
// La puse en MainActivity, y gracias a eso puedo usar hiltViewModel() en mis pantallas.