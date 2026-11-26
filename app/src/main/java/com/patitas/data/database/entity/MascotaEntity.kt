package com.patitas.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mascota",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_usuario"])]
)
data class MascotaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_mascota")
    val idMascota: Int = 0,
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int = 0,
    val nombre: String = "",
    val especie: String = "",
    val raza: String = "",
    @ColumnInfo(name = "fecha_nacimiento")
    val fechaNacimiento: String = "",   // aaaa-mm-dd
    @ColumnInfo(name = "peso_kg")
    val pesoKg: Float? = null,
    val foto: String = "",
    val alergias: String = "",
    val notas: String = "",
    @ColumnInfo(name = "es_favorita")
    val esFavorita: Boolean = false
)
