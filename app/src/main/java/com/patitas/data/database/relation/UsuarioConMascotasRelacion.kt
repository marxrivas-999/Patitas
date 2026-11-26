package com.patitas.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.patitas.data.database.entity.MascotaEntity
import com.patitas.data.database.entity.UsuarioEntity

data class UsuarioConMascotasRelacion(
    @Embedded val usuario: UsuarioEntity,
    @Relation(
        parentColumn = "id_usuario",
        entityColumn = "id_usuario"
    )
    val mascotas: List<MascotaEntity>
)
