package com.patitas.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Tabla USUARIO del modelo entidad-relación
@Entity(
    tableName = "usuario",
    indices = [Index(value = ["nombre_usuario"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int = 0,
    @ColumnInfo(name = "nombre_usuario")
    val nombreUsuario: String = "",
    @ColumnInfo(name = "contrasena_hash")
    val contrasenaHash: String = "",
    val nombre: String = "",
    val foto: String = "",
    @ColumnInfo(name = "recibir_notificaciones")
    val recibirNotificaciones: Boolean = true
)
