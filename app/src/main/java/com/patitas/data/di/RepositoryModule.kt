package com.patitas.data.di

import com.patitas.data.repository.MascotaRepositoryImp
import com.patitas.data.repository.RazaRepositoryImp
import com.patitas.data.repository.RelacionesRepositoryImp
import com.patitas.data.repository.UsuarioRepositoryImp
import com.patitas.domain.repository.MascotaRepository
import com.patitas.domain.repository.RazaRepository
import com.patitas.domain.repository.RelacionesRepository
import com.patitas.domain.repository.UsuarioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUsuarioRepository(
        usuarioRepository: UsuarioRepositoryImp
    ): UsuarioRepository

    @Binds
    @Singleton
    abstract fun bindMascotaRepository(
        mascotaRepository: MascotaRepositoryImp
    ): MascotaRepository

    // La interfaz RazaRepository se resuelve con RazaRepositoryImp (consume la API con Retrofit)
    @Binds
    @Singleton
    abstract fun bindRazaRepository(
        razaRepository: RazaRepositoryImp
    ): RazaRepository

    @Binds
    @Singleton
    abstract fun bindRelacionesRepository(
        relacionesRepository: RelacionesRepositoryImp
    ): RelacionesRepository
}
