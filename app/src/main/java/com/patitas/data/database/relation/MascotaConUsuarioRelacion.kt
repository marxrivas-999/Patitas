package com.patitas.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.patitas.data.database.entity.MascotaEntity
import com.patitas.data.database.entity.UsuarioEntity

data class MascotaConUsuarioRelacion(
    @Embedded val mascota: MascotaEntity,
    @Relation(
        parentColumn = "id_usuario",   // FK en MascotaEntity
        entityColumn = "id_usuario"    // PK en UsuarioEntity
    )
    val usuario: UsuarioEntity
)
