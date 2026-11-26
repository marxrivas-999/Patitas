package com.patitas.data.mapper

import com.patitas.data.database.entity.MascotaEntity
import com.patitas.data.database.entity.UsuarioEntity
import com.patitas.data.database.relation.MascotaConUsuarioRelacion
import com.patitas.data.database.relation.UsuarioConMascotasRelacion
import com.patitas.data.network.dto.RazasPerroDto
import com.patitas.domain.model.Mascota
import com.patitas.domain.model.MascotaConUsuario
import com.patitas.domain.model.Usuario
import com.patitas.domain.model.UsuarioConMascotas

fun UsuarioEntity.toDomain(): Usuario = Usuario(
    idUsuario = idUsuario,
    nombreUsuario = nombreUsuario,
    contrasenaHash = contrasenaHash,
    nombre = nombre,
    foto = foto,
    recibirNotificaciones = recibirNotificaciones
)

fun Usuario.toEntity(): UsuarioEntity = UsuarioEntity(
    idUsuario = idUsuario,
    nombreUsuario = nombreUsuario,
    contrasenaHash = contrasenaHash,
    nombre = nombre,
    foto = foto,
    recibirNotificaciones = recibirNotificaciones
)

fun MascotaEntity.toDomain(): Mascota = Mascota(
    idMascota = idMascota,
    idUsuario = idUsuario,
    nombre = nombre,
    especie = especie,
    raza = raza,
    fechaNacimiento = fechaNacimiento,
    pesoKg = pesoKg,
    foto = foto,
    alergias = alergias,
    notas = notas,
    esFavorita = esFavorita
)

fun Mascota.toEntity(): MascotaEntity = MascotaEntity(
    idMascota = idMascota,
    idUsuario = idUsuario,
    nombre = nombre,
    especie = especie,
    raza = raza,
    fechaNacimiento = fechaNacimiento,
    pesoKg = pesoKg,
    foto = foto,
    alergias = alergias,
    notas = notas,
    esFavorita = esFavorita
)

// Relación 1 a N: las favoritas salen primero y luego por nombre (igual que en la pantalla Inicio)
fun UsuarioConMascotasRelacion.toDomain(): UsuarioConMascotas = UsuarioConMascotas(
    usuario = usuario.toDomain(),
    mascotas = mascotas
        .map { it.toDomain() }
        .sortedWith(compareByDescending<Mascota> { it.esFavorita }.thenBy { it.nombre })
)

// Relación N a 1: la mascota con su dueño
fun MascotaConUsuarioRelacion.toDomain(): MascotaConUsuario = MascotaConUsuario(
    mascota = mascota.toDomain(),
    usuario = usuario.toDomain()
)

// API de razas: "bulldog" con subrazas ["boston", "french"] -> "Boston bulldog", "French bulldog"
fun RazasPerroDto.toDomain(): List<String> =
    message
        .flatMap { (raza, subrazas) ->
            if (subrazas.isEmpty()) listOf(raza) else subrazas.map { "$it $raza" }
        }
        .map { nombre -> nombre.replaceFirstChar { it.uppercase() } }
        .sorted()
