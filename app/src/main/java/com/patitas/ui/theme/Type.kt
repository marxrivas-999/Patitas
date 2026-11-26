package com.patitas.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.patitas.R

// Textos, botones y etiquetas: Poppins (archivos en res/font).
val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

// Títulos: la guía de estilo usa Baloo 2. Para usarla, copia baloo2_bold.ttf a res/font
// y cambia la definición de abajo para que apunte a ese archivo (misma forma que Poppins).
// Mientras tanto se usa Poppins Bold para que el proyecto compile sin archivos extra.
val TitulosFont = FontFamily(
    Font(R.font.poppins_bold, FontWeight.Bold)
)

val PatitasTypography = Typography(
    displayMedium  = TextStyle(fontFamily = TitulosFont, fontWeight = FontWeight.Bold, fontSize = 34.sp, lineHeight = 38.sp),
    headlineMedium = TextStyle(fontFamily = TitulosFont, fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 34.sp),
    headlineSmall  = TextStyle(fontFamily = TitulosFont, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 30.sp),
    titleLarge     = TextStyle(fontFamily = TitulosFont, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium    = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 22.sp),
    titleSmall     = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    bodyLarge      = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp),
    bodyMedium     = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 19.sp),
    bodySmall      = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 17.sp),
    labelLarge     = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium    = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall     = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 14.sp)
)
