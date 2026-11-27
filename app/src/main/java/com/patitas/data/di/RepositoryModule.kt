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
    @Binds //qué implementación usar para cada interfaz
    @Singleton
    abstract fun bindUsuarioRepository(
        usuarioRepository: UsuarioRepositoryImp
    ): UsuarioRepository

    @Binds
    @Singleton
    abstract fun bindMascotaRepository(
        mascotaRepository: MascotaRepositoryImp
    ): MascotaRepository

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


//8:Una clase no crea los objetos que necesita; los recibe desde fuera
// (normalmente por el constructor). Así el código está menos acoplado y
// es más fácil de probar y cambiar. Hilt es la librería de Google que automatiza esto.