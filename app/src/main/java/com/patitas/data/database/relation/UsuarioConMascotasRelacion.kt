package com.patitas.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.patitas.data.database.entity.MascotaEntity
import com.patitas.data.database.entity.UsuarioEntity

// UNO A MUCHOS (1 a N): un USUARIO (padre) con la lista de sus MASCOTAS (hijas).
// - @Embedded trae las columnas del usuario.
// - @Relation busca las mascotas cuya llave foránea (id_usuario) coincide con la llave primaria del usuario.
// Room NO garantiza el orden de la lista de mascotas; por eso se ordena en el mapper.
data class UsuarioConMascotasRelacion(
    @Embedded val usuario: UsuarioEntity,
    @Relation(
        parentColumn = "id_usuario",   // clave primaria de UsuarioEntity
        entityColumn = "id_usuario"    // columna de MascotaEntity que apunta al usuario (FK)
    )
    val mascotas: List<MascotaEntity>
)
