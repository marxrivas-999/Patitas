package com.patitas.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.patitas.data.database.entity.MascotaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MascotaDao {
    @Insert
    suspend fun insertarMascota(mascota: MascotaEntity)

    @Update
    suspend fun actualizarMascota(mascota: MascotaEntity)

    @Delete
    suspend fun eliminarMascota(mascota: MascotaEntity)

    @Query("SELECT * FROM mascota WHERE id_mascota = :id")
    fun getMascota(id: Int): Flow<MascotaEntity?>

    // Las favoritas aparecen primero
    @Query(
        "SELECT * FROM mascota WHERE id_usuario = :idUsuario " +
            "ORDER BY es_favorita DESC, nombre ASC"
    )
    fun obtenerMascotas(idUsuario: Int): Flow<List<MascotaEntity>>
}
