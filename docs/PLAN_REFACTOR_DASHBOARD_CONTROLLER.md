Plan de refactorización — DashboardController
Fecha: 2026-04-26

Resumen
- Archivo objetivo: src/controllers/DashboardController.java
- Objetivo: dividir responsabilidades, reducir código repetido, aplicar mejores prácticas (SRP, capas, try-with-resources), y mantener el comportamiento y la UI existentes.

Problemas detectados
- Concentración de responsabilidades: navegación, UI, accesos a BD, lógica de negocio, gestión de imágenes y generación de reportes en una sola clase.
- Código repetido: múltiples métodos fillComboBox*, CRUD similares para materiales/alturas/calibres/rombos.
- Gestión de BD frágil: conexión estática, ResultSet/PreparedStatement como campos de clase, recursos no siempre cerrados.
- SQL inseguro: concatenación en queries (p. ej. reportes), riesgo de inyección y errores difíciles de testear.
- Método monolítico para eventos: handleClicks() concentra todo el control de UI y lógica, es inmanejable.
- Lógica de I/O e imágenes acoplada al controlador: subida/lectura de archivos mezclada con lógica UI.
- Reportes acoplados: ImprimirReporte() mezcla template, query y visualización.

Arquitectura propuesta (capas)
- UI: controladores JavaFX delgados, uno por pantalla: Navigation, Profile, Empleados, Produccion, Historial, Materiales.
- Services (business): orquestan reglas y llamadas a DAOs (UsuariosService, ProduccionService, MaterialService, ReportService, AuthService).
- DAO / Repository: encapsulan acceso JDBC y mapping ResultSet->Model (UsuariosDao, LookupDao, ProduccionDao, HistorialDao, MaterialesDao, AlturasDao, CalibresDao, RombosDao).
- Config: ConnectionProvider central; extraer credenciales a config/config.properties.
- Util / Helpers: ImageUtils (ya existe), DateUtils (ya existe), UiHelpers (alerts, dialogs), ComboBoxLoader.
- Session: SessionManager para currentUser, en lugar de usar llamadas estáticas dispersas.

Mapa de extracción (métodos → nuevas clases/áreas)
- ProfileController
  - Perfil(), CambiarContraseña(), UpdateContraseña(), ComprobarPrimerSesion(), CambiarContraseñaPrimera(), updateImagenPerfil(), LimpiarPerfil().
- EmpleadosController + UsuariosService/UsuariosDao
  - UpdateTable(), TableValueEmpleados(), CodigoUsuario(), AgregarEmpleado/AgregarEmpleadoConImagen(), ModificarEmpleado/ModificarEmpleadoConImagen(), EliminarEmpleado(), PerfilEmpleado(), filtros (Filtro()).
- LookupDao / ComboBoxLoader
  - fillComboBoxPais(), fillComboBoxEstados(), fillComboBoxCiudades(), fillComboBoxGenero(), fillComboBoxTipoUsuario(), fillComboBoxPago(), fillComboBoxBanco(), fillComboBoxPeriodoPago(), fillComboBoxContrato(), fillComboBoxMaterial(), fillComboBoxAltura(), fillComboBoxCalibre(), fillComboBoxRombo().
- ProduccionController + ProduccionService
  - AgregarProduccion(), ModificarProduccion(), UpdateProduccionSemanal(), cleanProduccion(), TableProduccionS(), FechaActualProduccion(), DiasSemana().
- HistorialController + ReportService
  - UpdateHistorial(), UpdateMesHistorial(), UpdateFechaHistorial(), BuscarEmpleadoHistorial(), ImprimirReporte() → mover a ReportService (query parametrizada + plantilla).
- MaterialesController + MaterialService
  - TableMateriales(), Agregar/Modificar/Eliminar Materiales, Alturas, Calibres, Rombos.
- Navigation
  - Extraer la lógica de show/hide de panes y el switching que hoy está en handleClicks().

Plan de acción detallado (priorizado, con riesgo y estimados)
- Paso 0 — Preparación (bajo riesgo, 15–30 min)
  - crear rama: feature/refactor-dashboard
  - backup del proyecto y BD de pruebas
  - guardar este plan en repo (hecho)

- Paso 1 — Extracción de lookups (bajo riesgo, 1–3 horas) [Prioridad alta]
  - Crear LookupDao/ComboBoxLoader con un método genérico que devuelve ObservableList<String> desde una query y columna.
  - Reemplazar todos los fillComboBox* para usar LookupDao.
  - Verificar UI y chequear que los ComboBox cargan correctamente.

- Paso 2 — Encapsular conexión (bajo riesgo, 1–3 horas) [Prioridad alta]
  - Crear ConnectionProvider que lea config y devuelva Connection.
  - Eliminar la conexión estática del controlador.
  - Actualizar DAOs existentes para usar try-with-resources.

- Paso 3 — Mover CRUD del controlador a DAOs (medio riesgo, 1–2 días)
  - Mover operaciones SQL (insert/update/delete/select) que están en el controlador hacia UsuariosDao, MaterialesDao, AlturasDao, CalibresDao, RombosDao, ProduccionDao.
  - Añadir métodos claros: findById, findAll, insert, update, delete.
  - Reemplazar llamadas del controlador por llamadas a los DAOs.

- Paso 4 — Extraer Services (medio riesgo, 1–2 días)
  - Crear ProduccionService (orquesta inserciones y actualizaciones, validaciones), ReportService (genera reportes), MaterialService.
  - Mantener la lógica de negocio fuera de la UI.

- Paso 5 — Dividir controladores UI (medio/alto riesgo, 1–3 días)
  - Crear controladores por pantalla y mover bindings @FXML y handlers específicos.
  - Mantener los IDs FXML sin cambios; refactorizar handlers en funciones pequeñas.
  - Reemplazar handleClicks() por handlers más pequeños o por un NavigationController que solo haga pane switching.

- Paso 6 — Background tasks y responsividad (medio riesgo, 4–8 horas)
  - Usar JavaFX Task/Service para operaciones largas (generación de reportes, cargas de imágenes, consultas grandes).

- Paso 7 — Seguridad y limpieza (bajo riesgo, 4–8 horas)
  - Eliminar concatenaciones SQL; usar PreparedStatement parametrizado en todos los DAOs.
  - Reemplazar prints por logging (ya parcial).
  - Manejar excepciones con logs y mensajes de UI claros.

- Paso 8 — Tests y validación (medio riesgo, 1–3 días)
  - Escribir pruebas unitarias para DAOs (usar H2 o BD de pruebas), tests para Services (mock DAOs si aplica).
  - Pruebas manuales/funcionales: login, CRUD empleados, registrar producción, generar reporte.

- Paso 9 — Documentar y revisar (bajo riesgo, 2–4 horas)
  - Actualizar README con pasos para desarrollar y probar.
  - Checklist de QA antes de merge.

Prioridad inicial recomendada: Paso 1 → Paso 2 → Paso 3 (reduce duplicación y riesgos inmediatos).

Riesgos y mitigaciones
- Riesgo: romper funcionalidad por cambios en queries o IDs FXML.
  - Mitigación: commits pequeños, pruebas manuales tras cada paso, no renombrar IDs de FXML sin PR separado.
- Riesgo: regresión en reportes (queries dinámicas)
  - Mitigación: mover ImprimirReporte() a ReportService conservando la query parametrizada; probar con dataset de ejemplo.
- Riesgo: secretos en repo (config BD)
  - Mitigación: extraer credenciales a config/private.properties o variables de entorno.

Siguiente paso propuesto (acción inmediata)
- Implementar Paso 1: crear LookupDao/ComboBoxLoader y reemplazar fillComboBoxPais(), fillComboBoxEstados(), fillComboBoxCiudades(), fillComboBoxGenero() en src/controllers/DashboardController.java.
- Razón: es un cambio de bajo riesgo que reduce código repetido y prepara el terreno para mover la conexión y CRUD a DAOs.

Checklist de verificación tras cada cambio
- La UI arranca y no muestra excepciones en consola.
- Login y operaciones principales (CRUD empleados, registro de producción, generación de reporte) funcionan manualmente.
- No se añaden cambios a IDs de FXML sin nota en el plan.

Notas finales
- Mantener commits pequeños y reversibles.
- Documentar cada refactor en el PR (qué se movió, por qué, cómo probar).
- Si quieres, puedo empezar ahora mismo con Paso 1 y aplicar el primer patch.

— Fin del plan —
