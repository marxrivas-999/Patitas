# Patitas · Revisión 4 (Room + Hilt + Retrofit + relaciones + sensores)

Aplicación móvil para organizar los cuidados de las mascotas, con base de datos **Room (SQLite)** y **Hilt**
siguiendo el tutorial BD-room y el diseño de Figma.

## Tablas implementadas (modelo entidad-relación)
- **USUARIO** (`id_usuario`, `nombre_usuario`, `contrasena_hash`, `nombre`, `foto`, `recibir_notificaciones`)
- **MASCOTA** (`id_mascota`, `id_usuario` FK, `nombre`, `especie`, `raza`, `fecha_nacimiento`, `peso_kg`, `foto`, `alergias`, `notas`, `es_favorita`)
- Pendientes: TIPO_CUIDADO, CUIDADO, NOTIFICACION y CONSEJO.

## Integración de la Práctica 2 (solo con las tablas USUARIO y MASCOTA)
- **Relación de Room 1 a N** USUARIO → MASCOTA: `data/database/relation/UsuarioConMascotasRelacion` (`@Embedded` + `@Relation`),
  consulta con `@Transaction` en `UsuarioDao.obtenerUsuarioConMascotas`. Se muestra en Perfil → "Mis mascotas" (total y favoritas).
- **Pantalla "Relaciones en Room"** (Perfil → "Relaciones en Room"; equivale a `RelacionesScreen` de la práctica): muestra
  1 a N (cada usuario con sus mascotas) y N a 1 (cada mascota con su dueño). Usa `RelacionesDao`, `RelacionesRepository` y `RelacionesViewModel`.
- **Crear cuenta** (`ui/RegistroScreen`, enlace en el Login): inserta en USUARIO con la contraseña en hash SHA-256,
  valida usuario único y mínimo 8 caracteres, e inicia sesión al terminar.
- **Consumo de API con Retrofit** (https://dog.ceo/api/breeds/list/all): `data/network/` (`RazaApiService`, `dto/RazasPerroDto`),
  `data/di/NetworkModule`, `RazaRepository` / `RazaRepositoryImp` y `ui/RazasViewModel` (patrón UiState + UiEvent).
  Alimenta el desplegable "Raza" del formulario cuando la especie es Perro; sin internet usa la lista local.
- **Sensores del dispositivo** (`SensorManager`): `ui/SensoresScreen`, se abre desde Perfil → "Sensores del dispositivo".
- La base de datos NO cambió de versión (la relación no agrega tablas ni columnas).

## Usuario de prueba (se crea la primera vez que se abre la app)
- Usuario: `ana garcia`
- Contraseña: `12345678` (en la base se guarda su hash SHA-256)

## Pantallas
1. Bienvenida · 2. Inicio de sesión (consulta la tabla USUARIO) · 3. Inicio (Mis mascotas desde la tabla MASCOTA)
4. Agregar mascota (y Editar ficha, con eliminar) · 5. Perfil de mascota (favorita con corazón) · 8. Perfil y configuración (interruptor de notificaciones guardado en USUARIO, cambio de foto, cerrar sesión)
- Las pantallas 6 (Registrar cuidado), 7 (Historial), 9 (Notificaciones) y 10 (Consejos) dependen de las tablas pendientes; sus botones muestran el aviso "disponible próximamente".
- En Inicio, la tarjeta "Próximos cuidados" es de muestra hasta que exista la tabla CUIDADO.

## Estructura (patrón repository + UDF, igual que la práctica)
- `data/database/`: `AppDatabase`, `entity/` (UsuarioEntity, MascotaEntity), `relation/`, `dao/` (UsuarioDao, MascotaDao)
- `data/network/`: `RazaApiService` y `dto/`
- `data/repository/`, `data/mapper/`, `data/di/` (DataBaseModule, RepositoryModule)
- `domain/model/` y `domain/repository/`
- `ui/`: pantallas, `UsuarioViewModel`, `MascotaViewModel`, `Componentes.kt`
- `PatitasApp.kt` (`@HiltAndroidApp`) y `MainActivity` (`@AndroidEntryPoint`)

## Cómo abrirlo
File → Open → carpeta `Patitas`, esperar el Sync de Gradle (necesita internet) y ejecutar en un emulador API 29 o superior.
Las versiones de Gradle, KSP, Room, Hilt, Retrofit y OkHttp son las mismas de la Práctica 2.

## Notas
- Fuente Poppins incluida; los títulos usan Poppins Bold. Para usar Baloo 2 (como en Figma) agrega `baloo2_bold.ttf` en `res/font` y cámbialo en `ui/theme/Type.kt`.
