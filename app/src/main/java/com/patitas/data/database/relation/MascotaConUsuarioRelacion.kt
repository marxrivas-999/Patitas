package com.patitas.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.patitas.data.database.entity.MascotaEntity
import com.patitas.data.database.entity.UsuarioEntity

// MUCHOS A UNO (N a 1): una MASCOTA con el USUARIO dueño (la misma relación vista desde el lado "muchos").
// Es como ProductoConFicha de la práctica, pero el hijo (mascota) es el que trae la llave foránea.
data class MascotaConUsuarioRelacion(
    @Embedded val mascota: MascotaEntity,
    @Relation(
        parentColumn = "id_usuario",   // FK en MascotaEntity
        entityColumn = "id_usuario"    // PK en UsuarioEntity
    )
    val usuario: UsuarioEntity
)
