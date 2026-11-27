package com.patitas.domain.repository

import com.patitas.domain.model.Mascota
import kotlinx.coroutines.flow.Flow

interface MascotaRepository {
    fun obtenerMascotas(idUsuario: Int): Flow<List<Mascota>>
    fun obtenerMascota(id: Int): Flow<Mascota?>
    suspend fun insertarMascota(mascota: Mascota)
    suspend fun actualizarMascota(mascota: Mascota)
    suspend fun deleteMascota(mascota: Mascota)
}


/*14: Cinco principios de diseño para escribir código ordenado y fácil de mantener:
•	S (Responsabilidad única): cada clase hace una sola cosa.
•	O (Abierto/Cerrado): se puede ampliar sin modificar lo existente.
•	L (Sustitución de Liskov): una implementación puede reemplazar a su interfaz sin romper nada.
•	I (Segregación de interfaces): mejor varias interfaces pequeñas que una grande.
•	D (Inversión de dependencias): depender de abstracciones (interfaces), no de clases concretas.
En tu proyecto:
•	S: Seguridad solo hashea, SesionUsuario solo maneja la sesión, un DAO por entidad.
•	O: agregar otro repositorio o pantalla no obliga a tocar los demás.
•	L: cualquier *RepositoryImp puede usarse donde se pide su interfaz.
•	I: cuatro interfaces de repositorio separadas.
•	D: los ViewModels piden la interfaz (MascotaRepository) y Hilt entrega la implementación gracias
a @Binds.
 */
