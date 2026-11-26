package com.patitas.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.patitas.data.database.relation.MascotaConUsuarioRelacion
import com.patitas.data.database.relation.UsuarioConMascotasRelacion
import kotlinx.coroutines.flow.Flow


@Dao
interface RelacionesDao {


    @Transaction
    @Query("SELECT * FROM usuario ORDER BY nombre_usuario")
    fun obtenerUsuariosConMascotas(): Flow<List<UsuarioConMascotasRelacion>>


    @Transaction
    @Query("SELECT * FROM mascota ORDER BY nombre")
    fun obtenerMascotasConUsuario(): Flow<List<MascotaConUsuarioRelacion>>
}
