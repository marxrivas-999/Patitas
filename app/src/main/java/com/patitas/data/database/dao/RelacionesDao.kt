package com.patitas.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.patitas.data.database.relation.MascotaConUsuarioRelacion
import com.patitas.data.database.relation.UsuarioConMascotasRelacion
import kotlinx.coroutines.flow.Flow

// Consultas con relaciones (como RelacionesDao de la Práctica 2).
// @Transaction porque Room ejecuta más de una consulta por cada función.
// Flow: la pantalla se actualiza sola cuando cambia la base de datos.
@Dao
interface RelacionesDao {

    // Uno a muchos: cada usuario con sus mascotas
    @Transaction
    @Query("SELECT * FROM usuario ORDER BY nombre_usuario")
    fun obtenerUsuariosConMascotas(): Flow<List<UsuarioConMascotasRelacion>>

    // Muchos a uno: cada mascota con su dueño
    @Transaction
    @Query("SELECT * FROM mascota ORDER BY nombre")
    fun obtenerMascotasConUsuario(): Flow<List<MascotaConUsuarioRelacion>>
}
