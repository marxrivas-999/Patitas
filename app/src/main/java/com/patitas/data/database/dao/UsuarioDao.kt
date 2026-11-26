package com.patitas.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.patitas.data.database.entity.UsuarioEntity
import com.patitas.data.database.relation.UsuarioConMascotasRelacion
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertarUsuario(usuario: UsuarioEntity)

    @Update
    suspend fun actualizarUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuario WHERE nombre_usuario = :nombreUsuario COLLATE NOCASE LIMIT 1")
    suspend fun buscarPorNombreUsuario(nombreUsuario: String): UsuarioEntity?

    @Query("SELECT * FROM usuario WHERE id_usuario = :id")
    fun getUsuario(id: Int): Flow<UsuarioEntity?>


    @Transaction
    @Query("SELECT * FROM usuario WHERE id_usuario = :id")
    fun obtenerUsuarioConMascotas(id: Int): Flow<UsuarioConMascotasRelacion?>
}
