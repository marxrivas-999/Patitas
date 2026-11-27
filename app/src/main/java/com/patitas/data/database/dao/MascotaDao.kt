package com.patitas.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.patitas.data.database.entity.MascotaEntity
import kotlinx.coroutines.flow.Flow
//17:"@Entity convierte una data class en una tabla de SQLite. @Dao marca la interfaz con las
// operaciones sobre esas tablas
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


    @Query(
        "SELECT * FROM mascota WHERE id_usuario = :idUsuario " +
            "ORDER BY es_favorita DESC, nombre ASC"
    )
    fun obtenerMascotas(idUsuario: Int): Flow<List<MascotaEntity>>
}






/*7: Es cuando un objeto avisa a sus observadores cuando algo cambia. Yo lo implemento
con Flow y StateFlow. Mis DAO de Room devuelven Flow, el ViewModel lo expone como StateFlow
y la pantalla lo observa con collectAsStateWithLifecycle. Así, si agrego una mascota, la lista
se actualiza sola."*/