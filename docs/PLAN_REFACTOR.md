Plan de trabajo — Refactorización del proyecto "aceros"

Objetivo
- Entregar una refactorización incremental y segura que mejore la mantenibilidad, legibilidad y escalabilidad del proyecto sin cambiar la lógica de negocio ni la interfaz (FXML/UI).

Alcance
- Refactorizar código Java existente, organización de paquetes, manejo de recursos, pruebas y herramientas de calidad.
- Mantener intacta la lógica de negocio y los FXML; cualquier cambio en IDs de FXML se documentará y se hará con cuidado.

Principios y estándares a aplicar
- SOLID: Single Responsibility, Open/Closed, Liskov, Interface Segregation, Dependency Inversion.
- KISS: mantener las soluciones simples.
- DRY: eliminar duplicación mediante utilidades y capas comunes.
- YAGNI: evitar introducir dependencias/arquitecturas complejas si no son necesarias.
- Separation of Concerns (SoC): UI (controllers) vs servicios vs acceso a datos.
- Fail-fast y manejo explícito de excepciones.
- Uso de recursos seguros: `try-with-resources` para JDBC/IO.
- Evitar estado global (reemplazar `static` compartido por `SessionManager` o inyección).
- Ejecutar operaciones bloqueantes fuera del hilo de la UI (JavaFX `Task`/`Service`).
- Seguridad: credenciales fuera del código, uso de `PreparedStatement` y validaciones.

Arquitectura propuesta (capas)
- UI: FXML + `controllers` (controladores delgados, sin lógica de negocio ni queries).
- Services (business): `services` — orquestan operaciones, reglas y llamadas a DAOs.
- DAO / Repository: `dao` — encapsula todo acceso JDBC/ResultSet -> Model.
- Models: `models` — POJOs y objetos de dominio (no lógica de UI).
- Config: `config` — carga `config.properties`, gestion de credenciales (no commit al repo).
- Util / Helpers: `util` — utilidades reutilizables (fechas, IO, validaciones, imagenes).
- Exceptions: `exceptions` — excepciones específicas y mapeo.

Estructura de paquetes recomendada
- `aceros` (recursos FXML)
- `controllers` (JavaFX controllers)
- `models` (POJOs)
- `dao` (UsuariosDao, MaterialesDao, AlturasDao, CalibresDao, RombosDao, ProduccionDao, HistorialDao)
- `services` (AuthService, ProduccionService, EmpleadosService)
- `config` (AppConfig, ConnectionProvider)
- `util` (DateUtils, ImageUtils, ValidationUtils, DbUtils)
- `exceptions`
- `tests` (unit y integration)

Herramientas y dependencias sugeridas
- Java 11+ (preferible LTS: 17) o acorde a tu entorno.
- Build: conservar Ant/NetBeans o migrar a Maven/Gradle (opcional; migración será un paso adicional).
- Logging: `slf4j` + `logback` o `java.util.logging` si se prefiere mantener jar ligero.
- Conexión a BD: HikariCP (pooling) o al menos un `ConnectionProvider` centralizado.
- Testing: JUnit 5, Mockito.
- Calidad: formatter (e.g., google-java-format), Checkstyle/SpotBugs.
- Informes: cobertura (JaCoCo), reportes de tests.

Detalle de trabajo y pasos (incremental y verificable)
Cada paso es incremental: aplicar, verificar que la UI y la lógica se comportan igual, commit pequeño.

Fase 0 — Preparación
- Crear rama git: `feature/refactor-plan`.
- Hacer backup del proyecto y una copia de la base de datos de pruebas.
- Crear archivo de plan en memoria (hecho) y guardar copia en repo (`docs/PLAN_REFACTOR.md`).

Fase 1 — Correcciones críticas y seguridad (bajo riesgo)
Objetivo: reducir riesgos inmediatos.
Acciones:
  1.1. Reemplazar `System.out.println` por logging en todo el proyecto.
  1.2. Revisar y aplicar `try-with-resources` en todas las operaciones JDBC y streams (FileInputStream, OutputStream).
  1.3. Revisar uso de `PreparedStatement` y eliminar concatenaciones SQL con variables; parametrizar todas las consultas.
  1.4. Extraer credenciales y URL de BD a `config/config.properties` (no commitear secretos; usar `config/private.properties` o variables de entorno localmente).
Verificación: la aplicación arranca, login funciona, y no hay fugas de recursos en ejecución normal.

Fase 2 — Externalizar conexión y ConnectionProvider
Objetivo: centralizar conexiones y preparar pooling.
Acciones:
  2.1. Refactor `ConnectionUtil`: convertirlo en `ConnectionProvider` que solo provea `Connection` (opcional: integrar HikariCP).
  2.2. Mover toda lógica de queries de `ConnectionUtil` hacia DAOs (sacar métodos `getDataUsers`, etc.).
Verificación: DAOs usan `ConnectionProvider.getConnection()` y queries funcionan idénticos.

Fase 3 — Introducir DAOs (bajo → medio riesgo)
Objetivo: separar acceso a datos.
Acciones:
  3.1. Crear `UsuariosDao` con métodos: `findById`, `findAll`, `insert`, `update`, `delete`.
  3.2. Crear DAOs para `Materiales`, `Alturas`, `Calibres`, `Rombos`, `Produccion`, `Historial`.
  3.3. Mapear `ResultSet` → modelos en métodos privados (evitar duplicación).
  3.4. Reemplazar llamadas directas a `ConnectionUtil.getDataUsers()` por `UsuariosDao.findAll()` en controladores.
Verificación: pruebas manuales de tablas/Listviews y datos son iguales.

Fase 4 — Servicio de autenticación y SessionManager (bajo riesgo)
Objetivo: eliminar estado global `static` y centralizar sesión.
Acciones:
  4.1. Crear `AuthService.login(userid, password)` que use `UsuariosDao`.
  4.2. Crear `SessionManager` para almacenar `currentUser` (singleton controlado o inyectado a controllers).
  4.3. Actualizar `LoginController` para consumir `AuthService` y `SessionManager`.
Verificación: login sin cambios visibles; `LoginController` ya no mantiene `static` sesion.

Fase 5 — Refactor controllers (medio riesgo)
Objetivo: controladores delgados, UI separada de lógica.
Acciones:
  5.1. Reemplazar lógica de consulta directa por llamadas a `Services` (ej. `EmpleadosService.getAll()` que delega a `UsuariosDao`).
  5.2. Mover lógica pesada a background `Task`/`Service` para evitar congelamiento de UI (por ejemplo, carga de imágenes, consultas largas).
  5.3. Centralizar la carga de ComboBoxes: `ComboBoxLoader.load(cb, dao::findAll, mapper)`.
  5.4. Limpiar duplicaciones (ej.: `AgregarEmpleado`/`AgregarEmpleadoConImagen`) en un único método con param optional.
Verificación: comprobación de funcionalidades en la UI; tiempos de respuesta aceptables; no bloqueos.

Fase 6 — Factorizar utilidades y validaciones (bajo riesgo)
Acciones:
  6.1. Crear `ValidationUtils` para validaciones de formularios.
  6.2. Crear `ImageUtils` para subir/guardar/leer imágenes (no escribir archivos temporales con nombre fijo como `photo.png`).
  6.3. Crear `DateUtils` para formateos y conversiones.
Verificación: reemplazar código duplicado y pasar pruebas manuales de formularios e imágenes.

Fase 7 — Tests (medio riesgo)
Objetivo: añadir pruebas para proteger refactorizaciones.
Acciones:
  7.1. Escribir pruebas unitarias para DAOs (usar BD de prueba o H2 embebida) — validar mapping ResultSet → modelos.
  7.2. Tests unitarios para `AuthService`, `ValidationUtils`.
  7.3. Añadir tests de integración básicos que ejecuten flujos (login, CRUD empleado) en entorno controlado.
Verificación: pipeline ejecuta tests y los pasa.

Fase 8 — Calidad de código y CI (bajo riesgo)
Acciones:
  8.1. Añadir formateador y reglas Checkstyle; ejecutar localmente y en CI.
  8.2. Configurar GitHub Actions (o CI preferida) para: compilar, ejecutar tests, checkstyle.
  8.3. Documentar pasos de build/run en README.
Verificación: PRs con checks automáticos; estilo consistente.

Fase 9 — Revisión final y documentación
Acciones:
  9.1. Revisar commits, limpiar ramas, squash si aplica.
  9.2. Actualizar README con: requisitos, cómo configurar BD de pruebas, cómo ejecutar la app y tests.
  9.3. Documentar decisiones de arquitectura y guía de mantenimiento.

Criterios de aceptación
- Comportamiento funcional intacto (login, CRUDs, reportes, impresión).
- No cambios visibles en la UI salvo mejoras menores de rendimiento.
- Todas las consultas parametrizadas (no concatenación insegura).
- Recursos (Connections/Streams) cerrados correctamente.
- Logs en lugar de prints y con niveles adecuados.
- Tests automatizados básicos que cubran DAOs y lógica crítica.

Estimación de esfuerzo (aprox.)
- Fase 1: 4–10 horas
- Fase 2: 3–8 horas
- Fase 3: 1–3 días
- Fase 4: 4–8 horas
- Fase 5: 1–3 días
- Fase 6: 4–12 horas
- Fase 7: 1–3 días
- Fase 8: 4–12 horas
- Fase 9: 4–8 horas
(Estimaciones dependen del número exacto de pantallas y complejidad en `DashboardController`.)

Riesgos y mitigaciones
- Riesgo: romper funcionalidad por cambios en queries. Mitigación: tests + commits pequeños + backup DB.
- Riesgo: cambios en FXML/IDs. Mitigación: evitar renombrar IDs; si es necesario, hacerlo en PRs pequeños con verificación.
- Riesgo: tiempo mayor al estimado. Mitigación: priorizar las tareas críticas (fases 1–3) y postergar mejora profunda (DI/JPA) a futuro.

Buenas prácticas de commits y ramas
- Rama por tarea: `feature/refactor-<tarea>`.
- Commits pequeños, descriptivos y atómicos.
- PR con descripción, checklist y pasos de verificación.

Siguiente paso recomendado (inmediato)
- Empezar por Fase 1: aplicar `try-with-resources` y extraer credenciales a `config.properties`. Confirmar que la app arranca y el login funciona.

Anexos rápidos (recomendaciones técnicas)
- Evitar `new Image("file:photo.png")` con ruta fija — usar ruta temporal única o ByteArrayImage.
- Reemplazar `con.createStatement().executeQuery(...)` por `try (PreparedStatement ps = con.prepareStatement(...)) { ... }`.
- Evitar escribir archivos sin gestión de nombres temporales; usar `Files.createTempFile` cuando aplique.

---
Plan guardado por el asistente para referencia futura.
