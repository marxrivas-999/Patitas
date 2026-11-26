package com.patitas.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.patitas.data.database.dao.MascotaDao
import com.patitas.data.database.dao.RelacionesDao
import com.patitas.data.database.dao.UsuarioDao
import com.patitas.data.database.entity.MascotaEntity
import com.patitas.data.database.entity.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class, MascotaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun relacionesDao(): RelacionesDao
}
