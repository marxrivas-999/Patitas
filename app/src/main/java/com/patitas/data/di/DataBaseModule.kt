package com.patitas.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.patitas.data.database.AppDatabase
import com.patitas.data.database.dao.MascotaDao
import com.patitas.data.database.dao.RelacionesDao
import com.patitas.data.database.dao.UsuarioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
//12:•	@Database: la base de datos.
//•	@Entity: una tabla.
//•	@PrimaryKey, @ColumnInfo, @Index, @ForeignKey: definición de columnas y relaciones.
//•	@Dao: interfaz de operaciones.
//•	@Insert, @Update, @Delete, @Query: las operaciones.
//•	@Transaction, @Embedded, @Relation: consultas con relaciones.
@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    private val usuarioInicial = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            db.execSQL(
                "INSERT INTO usuario (nombre_usuario, contrasena_hash, nombre, foto, recibir_notificaciones) " +
                    "VALUES ('ana garcia', " +
                    "'ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f', " +
                    "'Ana García', '', 1)"
            )
        }
    }
//Se reutiliza la instancia
    @Singleton
    @Provides
    fun getInstance(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "patitas.db"
        )
            .addCallback(usuarioInicial)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    fun provideUsuarioDao(database: AppDatabase): UsuarioDao = database.usuarioDao()

    @Provides
    fun provideMascotaDao(database: AppDatabase): MascotaDao = database.mascotaDao()

    @Provides
    fun provideRelacionesDao(database: AppDatabase): RelacionesDao = database.relacionesDao()
}
