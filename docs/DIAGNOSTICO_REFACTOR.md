# Informe Diagnóstico de Refactorización — Proyecto "Aceros"

**Fecha:** Mayo 2026  
**Analizador:** GitHub Copilot (Claude Sonnet 4.6)  
**Tecnologías:** Java, JavaFX (FXML), NetBeans, MySQL, JasperReports  
**Estado del proyecto:** Universitario, sin buenas prácticas iniciales aplicadas de forma consistente.

---

## 1. Arquitectura Actual

El proyecto **no sigue una arquitectura de capas formal ni coherente**. Existe un intento parcial de separación que fue aplicado de manera inconsistente, produciendo una mezcla de patrones a medio camino donde las capas nominales no reflejan las responsabilidades reales.

| Capa declarada | Estado real encontrado |
|---|---|
| `controllers/` | Mezcla heterogénea de: controladores JavaFX, modelos de datos (POJOs con `SimpleStringProperty`), lógica de negocio y queries SQL directas. |
| `dao/` | Parcialmente implementado. Los DAOs retornan tipos JavaFX (`ObservableList<controllers.X>`) creando acoplamiento con la capa de UI. Algunos tienen lógica de negocio embebida (cálculo de edad en `UsuariosDao.save()`). |
| `services/` | Parcialmente implementado. Algunos servicios contienen queries SQL que deberían estar en DAOs (`UsuariosService` ejecuta SQL directamente en `isFirstSession`, `verifyPassword`, `changePassword`). |
| `models/` | Prácticamente vacío. Solo contiene `UsuarioDetalle.java`. Los demás 7 modelos están erróneamente en `controllers/`. |
| `database/` | `ConnectionUtil` centraliza la apertura de conexiones, pero `DashboardController` lo ignora y mantiene su propia `static Connection con` abierta indefinidamente. |
| `util/` | `DateUtils` e `ImageUtils` existen pero `ImageUtils` tiene una fuga de archivos temporales. `ValidationUtils` no existe. |
| `config/` | `AppConfig` está bien diseñado (carga `config.properties` con fallback a classpath), pero la ruta relativa `"config/config.properties"` falla si la app no se ejecuta desde el directorio raíz del proyecto. |

**Veredicto:** Arquitectura degradada. La estructura de paquetes es decorativa. El código real está concentrado en un único archivo de 3,455 líneas, haciendo la base de código prácticamente no mantenible en su estado actual.

---

## 2. Análisis Detallado de Code Smells

### 2.1 God Class — `DashboardController.java` (3,455 líneas) ⚠️ CRÍTICO

Es el problema dominante del proyecto. Este único archivo concentra las responsabilidades de **al menos 10 pantallas/módulos distintos**:

1. Navegación entre paneles (menú lateral)
2. CRUD completo de Empleados
3. Formulario de agregar/modificar empleado (con 20+ campos de formulario)
4. Pantalla de Producción semanal
5. Pantalla de Historial de producción
6. Generación e impresión de reportes JasperReports
7. CRUD de Materiales
8. CRUD de Alturas
9. CRUD de Calibres
10. CRUD de Rombos
11. Pantalla de Perfil de usuario
12. Pantalla de cambio de contraseña
13. Control de acceso basado en roles (RBAC) — visibilidad de botones según tipo de empleado

**Síntomas concretos:**

- **40+ campos `@FXML` declarados** en la misma clase, de pantallas que no tienen relación entre sí.
- **13 `ObservableList` sin tipo genérico** (raw types):
  ```java
  final ObservableList MaterialOpcion = FXCollections.observableArrayList();
  final ObservableList AlturaOpcion = FXCollections.observableArrayList();
  final ObservableList CalibreOpcion = FXCollections.observableArrayList();
  final ObservableList RomboOpcion = FXCollections.observableArrayList();
  final ObservableList EditarMaterialOpcion = FXCollections.observableArrayList();
  // ... y 8 más
  ```
- **Queries SQL directas en el controlador** que bypasean completamente la capa DAO ya existente:
  - `AgregarProduccion()` — INSERT directo con `con.prepareStatement(sql)`
  - `ModificarProduccion()` — UPDATE directo con `con.prepareStatement(sql)`
  - `EliminarEmpleado()` — DELETE directo con `con.prepareStatement(sql)`
  - `CodigoUsuario()` — SELECT directo con `con.createStatement().executeQuery(...)`
- **Lógica de negocio incrustada** que debería estar en servicios/utilidades:
  - Cálculo de edad con `Period.between()`
  - Formateo de fechas
  - Construcción de queries SQL para JasperReports
  - Verificación y aplicación de roles (`VerificarTipoEmpleado`, `DashboardSupervisor`, `DashboardGeneral`)
  - Lógica de primer inicio de sesión (`ComprobarPrimerSesion`)
- **`initialize()` llama a 25+ métodos** de inicialización encadenados, sin ninguna separación lógica.

---

### 2.2 Modelos en el paquete incorrecto

Siete clases que son modelos de datos (view-models con `SimpleStringProperty` / `SimpleIntegerProperty`) están declaradas en el paquete `controllers` en lugar de `models`:

| Clase | Ubicación actual | Ubicación correcta | Descripción |
|---|---|---|---|
| `Empleados.java` | `controllers` | `models` | Row model para `TableView<Empleados>` |
| `Materiales.java` | `controllers` | `models` | Row model para `TableView<Materiales>` |
| `Historial.java` | `controllers` | `models` | Row model para `TableView<Historial>` |
| `ProduccionSemanal.java` | `controllers` | `models` | Row model para `TableView<ProduccionSemanal>` |
| `Alturas.java` | `controllers` | `models` | Row model para `TableView<Alturas>` |
| `Rombos.java` | `controllers` | `models` | Row model para `TableView<Rombos>` |
| `Calibres.java` | `controllers` | `models` | Row model para `TableView<Calibres>` |
| `Sesion.java` | `controllers` | `services/` | Modelo de sesión — debería ser `SessionManager` |

Esta ubicación incorrecta fuerza a los DAOs a importar clases del paquete `controllers`, creando una **dependencia circular** entre capas: `dao` → `controllers` → `dao`.

---

### 2.3 Nomenclatura de métodos viola convenciones Java

Todos los métodos públicos de `DashboardController` usan **PascalCase** (convención de clases) en lugar de **camelCase** (convención de métodos):

```java
// MAL — parece constructor o clase
public void UpdateTable() { ... }
public void Filtro() { ... }
public void Perfil() { ... }
public void AgregarProduccion() { ... }
public void ModificarProduccion() { ... }
public void EliminarEmpleado() { ... }
public void DashboardGeneral() { ... }
public void DashboardSupervisor() { ... }
public void VerificarTipoEmpleado() { ... }
public void CodigoUsuario() { ... }
public void CambiarContraseña() { ... }  // además: ñ en nombre de método

// CORRECTO
public void updateTable() { ... }
public void filtrar() { ... }
```

Adicionalmente, `CambiarContraseña()` usa el carácter especial `ñ` en el nombre del método — práctico en NetBeans pero un problema en otros entornos y herramientas de análisis.

---

### 2.4 Estado global mutable — sesión compartida vía `static`

```java
// LoginController.java
private static String sesion;   // estado de sesión del usuario actual

public static String getSesion() { return sesion; }
public static void setSesion(String sesion) { LoginController.sesion = sesion; }

// DashboardController.java — consume la sesión desde otro controlador
private String usuario = LoginController.getSesion();
```

**Problemas:**
- `DashboardController` tiene una **dependencia oculta y en tiempo de construcción** sobre `LoginController`. Si el `DashboardController` se instancia antes de que `logIn()` se ejecute, `usuario` será `null`.
- El campo `static` en `LoginController` hace que `LoginController` sea un repositorio de estado global — viola SRP.
- Imposible de testear unitariamente sin mockear una clase estática.
- En un escenario con múltiples ventanas, el estado se corrompe.

---

### 2.5 Conexión a BD como campo estático — fuga de recursos garantizada

```java
// DashboardController.java
private static Connection con;
ResultSet resultSet = null;
PreparedStatement pst = null;

public DashboardController() throws SQLException {
    con = ConnectionUtil.getConnection();  // abre conexión en constructor
}
```

**Problemas graves:**
- La conexión se abre en el **constructor** y **nunca se cierra** durante toda la sesión.
- Al ser `static`, si se instancia múltiples veces (ej: al cerrar y reabrir el dashboard), `con` anterior queda huérfana sin cierre → **fuga de conexión**.
- Si el servidor MySQL hace timeout de la conexión ociosa, `con` queda en estado inválido y todas las operaciones subsiguientes fallarán con `SQLException: Connection is closed`.
- Incompatible con cualquier esquema de connection pooling (HikariCP, DBCP).
- El mismo patrón en `LoginController`:
  ```java
  private static Connection con;
  public LoginController() {
      try { con = ConnectionUtil.getConnection(); } catch (SQLException ex) { con = null; }
  }
  ```

---

### 2.6 Código duplicado — bloque de navegación post-login (DRY violado)

El bloque completo de "login exitoso → cerrar ventana → cargar dashboard.fxml → mostrar con animación" aparece **literalmente copiado** en dos lugares de `LoginController`:

**Instancia 1:** En `handleButtonAction()` (clic en botón "Ingresar")
**Instancia 2:** En el listener `tf_password.setOnKeyPressed()` (tecla ENTER)

```java
// Bloque duplicado ~15 líneas — exactamente igual en ambos lugares:
Node node = (Node) event.getSource();
Stage stage = (Stage) node.getScene().getWindow();
stage.close();
Parent root = FXMLLoader.load(getClass().getResource("/aceros/dashboard.fxml"));
Scene scene = new Scene(root);
scene.setFill(Color.TRANSPARENT);
stage.setScene(scene);
new animatefx.animation.ZoomIn(root).play();
stage.show();
```

Cualquier cambio (nueva pantalla, diferente animación, logging adicional) debe hacerse dos veces.

---

### 2.7 Código duplicado — llenado de formulario de empleado (DRY violado)

Los métodos `PerfilEmpleado()` y `PerfilEmpleadoProduccion()` en `DashboardController` contienen **exactamente el mismo bloque de ~35 líneas** que puebla los `TextField`s y `ComboBox`es del formulario de empleado a partir de un `UsuarioDetalle`:

```java
tbCodigoUsuarioAgregar.setText(u.getUsuarioId());
tbNombreEmpleado.setText(u.getNombre());
tbAPaternoEmpleado.setText(u.getApellidoPaterno());
// ... 30 líneas más idénticas
```

Cada modificación al formulario (agregar un campo, cambiar validación) requiere actualizar ambos métodos.

---

### 2.8 DAOs que retornan tipos de la capa UI — acoplamiento circular

Los DAOs en la capa de acceso a datos importan y retornan clases del paquete `controllers`:

```java
// dao/UsuariosDao.java
import controllers.Empleados;  // ← DAO importa clase de UI
public static ObservableList<Empleados> getAll() { ... }

// dao/ProduccionDao.java
import controllers.ProduccionSemanal;  // ← DAO importa clase de UI
public static ObservableList<ProduccionSemanal> getProduccionSemana(String s) { ... }

// dao/HistorialDao.java
import controllers.Historial;  // ← DAO importa clase de UI
public static ObservableList<Historial> getHistorial(String s, String de, String a) { ... }
```

**Consecuencias:**
- Dependencia circular: `controllers` → `dao` → `controllers`.
- Imposible compilar los DAOs sin el classpath completo de JavaFX.
- Imposible escribir tests unitarios para los DAOs sin el runtime de JavaFX.
- Los DAOs están acoplados a la representación visual (propiedades JavaFX) en lugar de trabajar con POJOs simples.

---

### 2.9 Raw types en colecciones — 13 instancias

```java
// Todos en DashboardController, todos sin tipo genérico:
final ObservableList MaterialOpcion = FXCollections.observableArrayList();
final ObservableList AlturaOpcion = FXCollections.observableArrayList();
final ObservableList CalibreOpcion = FXCollections.observableArrayList();
final ObservableList RomboOpcion = FXCollections.observableArrayList();
final ObservableList EditarMaterialOpcion = FXCollections.observableArrayList();
final ObservableList EditarAlturaOpcion = FXCollections.observableArrayList();
final ObservableList EditarCalibreOpcion = FXCollections.observableArrayList();
final ObservableList EditarRomboOpcion = FXCollections.observableArrayList();
// ... y 5 más para empleados
```

El compilador no puede verificar el tipo de los elementos. Una asignación incorrecta fallará en runtime con `ClassCastException` en lugar de en tiempo de compilación.

---

### 2.10 Bug activo — comparación de Strings con `==` en `UpdateMesHistorial()`

```java
// DashboardController.java — UpdateMesHistorial()
cbHistorialMes.valueProperty().addListener((newValue) -> {
    if(cbHistorialMes.getValue() == "ENERO"){    // BUG: operador de identidad, no de igualdad
        ...
    }
    if(cbHistorialMes.getValue() == "FEBRERO"){  // BUG
        ...
    }
    // ... 12 comparaciones con == todas potencialmente rotas
});
```

La comparación `==` en Java compara la **referencia de memoria** del objeto `String`, no su contenido. Los Strings que vienen de un `ComboBox` cargado desde la BD o desde una `ObservableList` **no están en el string pool**, por lo que `==` retorna `false` aunque el valor sea "ENERO". El filtro de historial por mes **nunca funcionará de forma confiable**.

---

### 2.11 `System.out.println` residual

```java
// DashboardController.java — AgregarProduccion()
System.out.println("AGREGAR PRODUCCION BOTON PRESSED");
```

Un `println` de depuración quedó en código de producción. El proyecto ya usa `java.util.logging` en el resto, por lo que esta línea es un remanente olvidado.

---

## 3. Problemas de Seguridad y Corrección

### 3.1 🔴 SQL Injection activa — OWASP A03:2021

**Ubicación:** `DashboardController.java` → método `ImprimirReporte()`

```java
String autor = tbCodigoHistorial.getText();  // valor directo del usuario
String de    = fechaDe.toString();
String a     = fechaA.toString();

String Query = "select p.id, p.fecha_registro, ... "
    + "where p.autor_id = '" + autor + "'";  // ← CONCATENACIÓN DIRECTA

if (!de.isEmpty() && !a.isEmpty()) {
    Query += " and (p.fecha_registro BETWEEN '" + de + "' AND '" + a + "')";  // ← CONCATENACIÓN
}
```

El valor del campo `tbCodigoHistorial` (un `TextField` editable por el usuario) se concatena directamente en la query SQL que se pasa a JasperReports. Un valor malicioso como `' OR '1'='1` puede exponer todos los registros de la tabla. Esta es una vulnerabilidad **explotable activamente** si la aplicación es accesible por múltiples usuarios.

---

### 3.2 🔴 Contraseñas almacenadas en texto plano

**Ubicación:** Login en `LoginController.logIn()` y toda interacción con la columna `password` de la tabla `usuarios`.

```java
String sql = "SELECT * FROM usuarios Where usuario_id = ? and password = ?";
// El password se compara tal cual, sin hash
```

No hay ningún mecanismo de hashing (bcrypt, PBKDF2, SHA-256 con salt). Si la base de datos MySQL es comprometida (dump, acceso directo, backup expuesto), **todas las contraseñas de todos los usuarios quedan expuestas en claro**.

---

### 3.3 Fuga de archivos temporales en cada visualización de imagen

**Ubicación:** `util/ImageUtils.java` → `fromBytes()`

```java
public static Image fromBytes(byte[] data) throws IOException {
    Path tmp = Files.createTempFile("profile-", ".png");  // crea archivo temporal
    Files.write(tmp, data);
    return new Image(tmp.toUri().toString());
    // ← NUNCA se llama tmp.toFile().delete() ni se registra un shutdown hook
}
```

Cada vez que se muestra una imagen de perfil de empleado (al hacer clic en la tabla, al buscar en producción, al abrir el perfil), se crea un nuevo archivo `.png` temporal en el directorio temp del OS **que nunca se elimina**. En una sesión de trabajo normal con 20-30 búsquedas de empleados, se acumulan docenas de archivos huérfanos.

**Solución correcta:**
```java
public static Image fromBytes(byte[] data) {
    return new Image(new ByteArrayInputStream(data));  // cero archivos temporales
}
```

---

### 3.4 Recursos JDBC no liberados — `EliminarEmpleado()`

```java
public void EliminarEmpleado(){
    String sql = "delete from usuarios where usuario_id = ?";
    String in = Integer.toString(indexEmpleado);
    try{
        pst = con.prepareStatement(sql);  // pst es campo de instancia (compartido)
        pst.setString(1, in);
        pst.execute();
        UpdateTable();
    } catch (Exception e){
        // BLOQUE CATCH VACÍO — excepción silenciada completamente
    }
    // pst nunca se cierra: fuga de PreparedStatement
}
```

**Problemas:**
1. `pst` es un campo de instancia. Si se llama a `EliminarEmpleado()` dos veces, el primer `PreparedStatement` queda abierto sin cerrar.
2. El bloque `catch` está vacío: si la eliminación falla (constraint FK, conexión muerta, permisos), **el usuario no recibe ninguna notificación y la UI actúa como si la operación hubiera tenido éxito** (llama a `UpdateTable()` que recarga los datos sin cambios).
3. Usa `con` — la conexión estática que puede estar en estado inválido.

---

### 3.5 Recursos JDBC no liberados — `CodigoUsuario()`

```java
public void CodigoUsuario(){
    Connection con;
    try {
        con = ConnectionUtil.getConnection();       // abre conexión nueva
        ResultSet rs = con.createStatement()
                         .executeQuery("select * from usuarios");  // Statement tampoco se cierra
        while (rs.next()){
            dataList.add(new Empleados(...));
            // ...
        }
        // ← NO HAY finally{}, NO HAY try-with-resources
        // con, Statement y rs NUNCA se cierran
    } catch (Exception ex) {
        Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
    }
}
```

Este método abre una conexión nueva en cada llamada y **nunca la cierra**. Se llama en `initialize()`, lo que significa que en cada carga del dashboard se abre una conexión JDBC huérfana. Adicionalmente, esta query duplica exactamente `UsuariosDao.getAll()` que ya existe y sí cierra los recursos.

---

### 3.6 Typo en nombre de columna de BD

**Ubicación:** `services/UsuariosService.java` → `isFirstSession()`

```java
String sql = "select pimera_sesion from usuarios where usuario_id = ?";
//                   ^^^^^ falta "r" — debería ser "primera_sesion"
```

Este typo probablemente refleja el nombre real de la columna en la BD (el typo está en la BD, no solo en el código). Cualquier corrección requiere una migración coordinada de BD + código.

---

### 3.7 `UsuariosService` contiene acceso directo a BD — viola SRP

Los métodos de servicio deberían orquestar operaciones de negocio delegando el acceso a datos al DAO. En lugar de eso, `UsuariosService` ejecuta SQL directamente:

```java
// services/UsuariosService.java
public static boolean isFirstSession(String usuario) {
    String sql = "select pimera_sesion from usuarios where usuario_id = ?";
    try (Connection con = ConnectionUtil.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {  // ← SQL en el Service
        // ...
    }
}

public static boolean verifyPassword(String usuario, String password) {
    String sql = "select 1 from usuarios where usuario_id = ? and password = ?";
    // ← SQL en el Service
}

public static boolean changePassword(String usuario, String newPassword) {
    String sql = "update usuarios set password=?, pimera_sesion='1' where usuario_id=?";
    // ← SQL en el Service
}
```

Estos tres métodos deberían delegar a `UsuariosDao`, no construir sus propias queries.

---

### 3.8 `ModificarProduccion()` en controlador — bypasea el DAO

```java
// DashboardController.java — ModificarProduccion()
String sql = "update produccion set material= ?, calibre=?, ... where id=?";
try (PreparedStatement ps = con.prepareStatement(sql)) {
    // usa 'con' — la conexión estática posiblemente inválida
    ps.setString(1, material);
    // ...
}
```

`ProduccionDao.update()` ya existe y hace exactamente esto, pero el controlador lo ignora y ejecuta la query directamente usando la conexión estática `con`. Resultado: **dos implementaciones paralelas** de la misma lógica que pueden divergir.

---

## 4. Tabla de Impacto de Riesgo

| Componente | Criticidad | Fragilidad | Justificación |
|---|---|---|---|
| `DashboardController.java` (3,455 líneas) | **🔴 CRÍTICA** | **🔴 MUY ALTA** | Punto único de fallo para toda la aplicación. Cualquier cambio tiene efectos colaterales impredecibles. |
| `ImprimirReporte()` — SQL Injection | **🔴 CRÍTICA** | **🔴 ALTA** | Vulnerabilidad de seguridad activa y explotable. |
| Contraseñas en texto plano | **🔴 CRÍTICA** | **🔴 ALTA** | Un dump de BD expone todas las credenciales. |
| `LoginController.logIn()` | **🔴 CRÍTICA** | **🟠 ALTA** | Controla el acceso a toda la aplicación. Estado `static` frágil. |
| `private static Connection con` | **🟠 ALTA** | **🔴 MUY ALTA** | Fuga de conexión garantizada. Puede dejar la app inoperante por timeout. |
| `EliminarEmpleado()` | **🟠 ALTA** | **🔴 ALTA** | Excepción silenciada. El usuario no sabe si la operación falló. |
| `CodigoUsuario()` fuga JDBC | **🟠 ALTA** | **🔴 ALTA** | Fuga de conexión en cada carga del dashboard. |
| `UsuariosDao.save()` (29 parámetros) | **🟠 ALTA** | **🟠 MEDIA** | Muy difícil de mantener. Cualquier reordenamiento de parámetros es un bug silencioso. |
| Bug `==` en `UpdateMesHistorial()` | **🟡 MEDIA** | **🔴 ALTA** | Funcionalidad de filtrado por mes completamente rota sin error visible. |
| Fuga de archivos temp en `ImageUtils` | **🟡 MEDIA** | **🟠 MEDIA** | Degradación gradual del sistema de archivos del OS. |
| Código duplicado login | **🟡 MEDIA** | **🟡 MEDIA** | Divergencia eventual entre los dos flujos. |
| Modelos en paquete `controllers/` | **🟢 BAJA** | **🟡 MEDIA** | Impedimento para testing. Dependencias circulares entre capas. |
| `System.out.println` residual | **🟢 BAJA** | **🟢 BAJA** | Ruido en logs. Sin impacto funcional. |

---

## 5. Inventario Completo de Lógica de Negocio a Extraer de Controladores

La siguiente lógica existe actualmente en controladores (capas de UI) y debe migrar a las capas correctas:

| # | Lógica actual (en controlador) | Clase/método actual | Dónde debe vivir |
|---|---|---|---|
| 1 | Query de autenticación de usuario | `LoginController.logIn()` | `UsuariosDao.authenticate()` + `AuthService.login()` |
| 2 | Estado de sesión del usuario logueado | `LoginController.static sesion` | `SessionManager.getCurrentUserId()` |
| 3 | INSERT de producción | `DashboardController.AgregarProduccion()` | `ProduccionDao.insert()` (ya existe, se bypasea) |
| 4 | UPDATE de producción | `DashboardController.ModificarProduccion()` | `ProduccionDao.update()` (ya existe, se bypasea) |
| 5 | DELETE de empleado | `DashboardController.EliminarEmpleado()` | `UsuariosDao.delete()` (crear) |
| 6 | SELECT de todos usuarios + cálculo de ID siguiente | `DashboardController.CodigoUsuario()` | `UsuariosDao.getAll()` (ya existe) + `UsuariosDao.getNextId()` |
| 7 | Construcción de SQL para JasperReports | `DashboardController.ImprimirReporte()` | `ReportService.printProduccionReport()` con `PreparedStatement` |
| 8 | Verificar si es primera sesión | `UsuariosService.isFirstSession()` con SQL directo | `UsuariosDao.isFirstSession()` |
| 9 | Verificar contraseña actual | `UsuariosService.verifyPassword()` con SQL directo | `UsuariosDao.verifyPassword()` |
| 10 | Cambiar contraseña | `UsuariosService.changePassword()` con SQL directo | `UsuariosDao.changePassword()` |
| 11 | Cálculo de edad a partir de fecha de nacimiento | `UsuariosDao.save()` (en el DAO) | `UsuarioDetalle.calcularEdad()` o `DateUtils.calcularEdad()` |
| 12 | Lógica de primer inicio de sesión + forzar cambio | `DashboardController.ComprobarPrimerSesion()` | `AuthService.requiresPasswordChange()` |
| 13 | Mapeo nombre de mes → rango de fechas | `DashboardController.UpdateMesHistorial()` | `DateUtils.mesRangeOf(String mes, int anio)` |
| 14 | Verificación y aplicación de permisos por rol | `DashboardController.VerificarTipoEmpleado()` | `RolePermissions.apply(String tipo, DashboardController controller)` o mantener en controlador con extracción a helper |
| 15 | Carga de 13 ComboBoxes con datos de BD | 13 métodos `fillComboBox*` en `DashboardController` | `ComboBoxLoader.load(ComboBox, Supplier<List<String>>)` helper |

---

## 6. Plan de Refactorización Detallado

### Principios guía
- Cambios incrementales y verificables: cada fase debe dejar la aplicación compilando y funcionando.
- No modificar FXMLs ni cambiar IDs de controles (riesgo alto de regresión visual).
- Commits pequeños por cada paso.
- Rama por fase: `feature/refactor-fase-N`.

---

### FASE 0 — Preparación
**Objetivo:** Establecer un entorno seguro antes de tocar una sola línea de código.  
**Riesgo de regresión:** Ninguno  
**Tiempo estimado:** 15–30 minutos

#### Paso 0.1 — Backup del proyecto y la base de datos
✅ **Ya realizado por el usuario.**  
Para referencia futura: siempre hacer backup antes de iniciar una fase nueva.

#### Paso 0.2 — Crear rama git por fase
```bash
git checkout -b feature/refactor-fase-1
```
Usar una rama por fase. Al terminar y verificar, hacer merge a `main` con un PR descriptivo. Si algo sale mal, `git checkout main` descarta todo sin consecuencias.

#### Paso 0.3 — Verificar que la app arranca limpiamente desde cero
Antes de modificar cualquier cosa, confirmar:
- La app compila sin warnings críticos.
- El login funciona.
- Se puede registrar producción y generar un reporte.

Esto establece la **línea base** contra la cual verificar cada fase.

---

### FASE 1 — Correcciones Críticas de Seguridad y Recursos
**Objetivo:** Eliminar vulnerabilidades activas y fugas de recursos sin modificar la UI ni el flujo de negocio.  
**Riesgo de regresión:** Bajo  
**Tiempo estimado:** 4–8 horas

#### Paso 1.1 — Eliminar SQL Injection en `ImprimirReporte()`
**Archivo:** `src/controllers/DashboardController.java`

**Problema actual:**
```java
String Query = "... where p.autor_id = '" + autor + "'";
if (!de.isEmpty() && !a.isEmpty()) {
    Query += " and (p.fecha_registro BETWEEN '" + de + "' AND '" + a + "')";
}
JRDesignQuery updateQuery = new JRDesignQuery();
updateQuery.setText(Query);
jdesign.setQuery(updateQuery);
JasperReport jreport = JasperCompileManager.compileReport(jdesign);
JasperPrint jprint = JasperFillManager.fillReport(jreport, params, con);
```

**Solución:** Usar parámetros de JasperReports (`$P{param}`) en el JRXML y pasar los valores como parámetros del mapa. Alternativamente, obtener el `ResultSet` con `PreparedStatement` y pasarlo como `JRResultSetDataSource`:

```java
String sql = "select ... from produccion p left join usuarios u on p.autor_id = u.usuario_id "
           + "where p.autor_id = ?";
// Agregar condición de fecha solo si aplica
List<Object> paramValues = new ArrayList<>();
paramValues.add(autor);
if (!de.isEmpty() && !a.isEmpty()) {
    sql += " and (p.fecha_registro BETWEEN ? AND ?)";
    paramValues.add(de);
    paramValues.add(a);
}
sql += " order by p.fecha_registro";

try (Connection conn = ConnectionUtil.getConnection();
     PreparedStatement ps = conn.prepareStatement(sql)) {
    for (int i = 0; i < paramValues.size(); i++) {
        ps.setObject(i + 1, paramValues.get(i));
    }
    try (ResultSet rs = ps.executeQuery()) {
        JRResultSetDataSource dataSource = new JRResultSetDataSource(rs);
        JasperPrint jprint = JasperFillManager.fillReport(jreport, params, dataSource);
        JasperViewer.viewReport(jprint, false);
    }
}
```

#### Paso 1.2 — Cerrar recursos en `CodigoUsuario()`
**Archivo:** `src/controllers/DashboardController.java`

Reemplazar el método completo por una llamada a `UsuariosDao.getAll()`:

```java
public void codigoUsuario() {
    dataList.setAll(UsuariosDao.getAll());
    if (!dataList.isEmpty()) {
        int nextId = dataList.stream()
            .mapToInt(e -> e.getEmpIdUsuario())
            .max().orElse(0) + 1;
        tbCodigoUsuarioAgregar.setText(String.valueOf(nextId));
    }
}
```

#### Paso 1.3 — Corregir `EliminarEmpleado()` — agregar try-with-resources y feedback
```java
public void eliminarEmpleado() {
    String id = Integer.toString(indexEmpleado);
    try {
        UsuariosDao.delete(id);  // nuevo método en UsuariosDao
        updateTable();
    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Error al eliminar empleado id=" + id, ex);
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error al eliminar");
        alert.setContentText("No se pudo eliminar el empleado: " + ex.getMessage());
        alert.showAndWait();
    }
}
```

#### Paso 1.4 — Eliminar `private static Connection con` de `DashboardController`
Eliminar los campos `private static Connection con`, `ResultSet resultSet`, `PreparedStatement pst`. Todos los métodos que usan `con` directamente (`AgregarProduccion`, `ModificarProduccion`) deben obtener conexiones locales dentro de un `try-with-resources`, o — preferiblemente — delegar al DAO existente.

#### Paso 1.5 — Corregir `ImageUtils.fromBytes()` — eliminar archivos temporales
```java
// util/ImageUtils.java
public static Image fromBytes(byte[] data) {
    if (data == null || data.length == 0) return null;
    return new Image(new ByteArrayInputStream(data));
}
```

#### Paso 1.6 — Corregir comparación `==` en `UpdateMesHistorial()`
Reemplazar todas las comparaciones `==` por `.equals()`:
```java
if ("ENERO".equals(cbHistorialMes.getValue())) { ... }
if ("FEBRERO".equals(cbHistorialMes.getValue())) { ... }
// ... 12 casos
```
O mejor aún, refactorizar a un `switch` (Java 14+ con switch expressions) o `EnumMap<Month, LocalDate[]>`.

#### Paso 1.7 — Eliminar `System.out.println` residual
```java
// Eliminar esta línea en AgregarProduccion():
System.out.println("AGREGAR PRODUCCION BOTON PRESSED");
// Reemplazar por:
LOGGER.log(Level.FINE, "AgregarProduccion button pressed");
```

**Verificación Fase 1:** La app arranca, login funciona, CRUD de empleados funciona, reporte se genera, imágenes de perfil se cargan. Sin fugas JDBC ni archivos temporales.

---

### FASE 2 — SessionManager y eliminación de estado `static`
**Objetivo:** Eliminar la dependencia de `LoginController.static sesion` y centralizar la gestión de sesión.  
**Riesgo de regresión:** Medio  
**Tiempo estimado:** 4–6 horas

#### Paso 2.1 — Crear `SessionManager`
**Nuevo archivo:** `src/services/SessionManager.java`

```java
package services;

public final class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();

    private String currentUserId;
    private String tipoEmpleado;

    private SessionManager() {}

    public static SessionManager getInstance() { return INSTANCE; }

    public String getCurrentUserId() { return currentUserId; }
    public String getTipoEmpleado() { return tipoEmpleado; }

    public void setCurrentUser(String userId, String tipo) {
        this.currentUserId = userId;
        this.tipoEmpleado = tipo;
    }

    public void clear() {
        this.currentUserId = null;
        this.tipoEmpleado = null;
    }

    public boolean isLoggedIn() { return currentUserId != null; }
}
```

#### Paso 2.2 — Crear `AuthService`
**Nuevo archivo:** `src/services/AuthService.java`

```java
package services;

import dao.UsuariosDao;
import models.UsuarioDetalle;

public class AuthService {

    public enum LoginResult { SUCCESS, INVALID_CREDENTIALS, EMPTY_FIELDS, ERROR }

    public static LoginResult login(String userId, String password) {
        if (userId == null || userId.isBlank() || password == null || password.isBlank()) {
            return LoginResult.EMPTY_FIELDS;
        }
        try {
            // Delegar autenticación al DAO (nuevo método)
            boolean valid = UsuariosDao.authenticate(userId, password);
            if (!valid) return LoginResult.INVALID_CREDENTIALS;

            UsuarioDetalle u = UsuariosDao.findById(userId);
            String tipo = (u != null) ? u.getTipoEmpleado() : "";
            SessionManager.getInstance().setCurrentUser(userId, tipo);
            return LoginResult.SUCCESS;
        } catch (Exception ex) {
            return LoginResult.ERROR;
        }
    }

    public static void logout() {
        SessionManager.getInstance().clear();
    }
}
```

#### Paso 2.3 — Actualizar `LoginController`
- Eliminar `private static String sesion` y `private static Connection con`.
- Eliminar el constructor con side-effects.
- Refactorizar `logIn()` para delegar a `AuthService.login()`.
- Extraer el bloque de navegación duplicado a un método privado `navigateToDashboard(Stage stage)`.

#### Paso 2.4 — Actualizar `DashboardController`
- Reemplazar `LoginController.getSesion()` por `SessionManager.getInstance().getCurrentUserId()`.
- El campo `private String usuario` queda: `private final String usuario = SessionManager.getInstance().getCurrentUserId();`

**Verificación Fase 2:** Login, primer inicio de sesión, cambio de contraseña, comportamiento según rol — todo idéntico al anterior.

---

### FASE 3 — Migración de modelos al paquete correcto
**Objetivo:** Mover los 7 view-models de `controllers/` a `models/`. Eliminar dependencia circular DAO → controllers.  
**Riesgo de regresión:** Medio (requiere actualizar todos los imports)  
**Tiempo estimado:** 2–4 horas

#### Paso 3.1 — Mover las 7 clases
Usar la función "Refactor → Move Class" de NetBeans para mover automáticamente y actualizar imports:
- `controllers.Empleados` → `models.Empleados`
- `controllers.Materiales` → `models.Materiales`
- `controllers.Historial` → `models.Historial`
- `controllers.ProduccionSemanal` → `models.ProduccionSemanal`
- `controllers.Alturas` → `models.Alturas`
- `controllers.Rombos` → `models.Rombos`
- `controllers.Calibres` → `models.Calibres`

#### Paso 3.2 — Actualizar DAOs
Los DAOs (`UsuariosDao`, `ProduccionDao`, `HistorialDao`, `MaterialesDao`, `AlturasDao`, `CalibresDao`, `RombosDao`) actualizan automáticamente sus imports de `controllers.X` a `models.X`.

#### Paso 3.3 — Agregar tipos genéricos a las ObservableLists
```java
// ANTES (raw type)
final ObservableList MaterialOpcion = FXCollections.observableArrayList();

// DESPUÉS (tipado)
final ObservableList<String> MaterialOpcion = FXCollections.observableArrayList();
```

**Verificación Fase 3:** Compilación limpia. Todas las tablas de la UI siguen mostrando datos.

---

### FASE 4 — Descomposición del God Class `DashboardController`
**Objetivo:** Reducir `DashboardController` de 3,455 líneas a ~200 líneas que solo orquesten la navegación entre paneles. Extraer cada módulo a su propio controlador.  
**Riesgo de regresión:** Alto (fase más larga — ejecutar en sub-pasos verificables)  
**Tiempo estimado:** 2–4 días

> **Estrategia:** El FXML `dashboard.fxml` define un único `Pane` raíz con sub-`Pane`s para cada sección. **No se modifican los FXMLs.** En cambio, se crean clases de controlador separadas que operan sobre los mismos `@FXML` fields, y `DashboardController` los instancia y les delega llamadas a través de métodos del paquete (package-private) o de interfaces.
>
> Alternativa más limpia (mayor esfuerzo): dividir el dashboard en múltiples FXML con `fx:include`, cada uno con su propio controlador. Esto requiere modificar el FXML principal pero es la solución más escalable.

#### Sub-paso 4.1 — Crear `EmpleadosController`
**Nuevo archivo:** `src/controllers/EmpleadosController.java`

Mover desde `DashboardController`:
- Todos los campos `@FXML` de la sección de empleados (tabla, filtros, formulario de agregar/editar, imagen de perfil)
- `UpdateTable()` → `updateTable()`
- `TableValueEmpleados()` → `setupEmpleadosTable()`
- `AgregarEmpleado()`, `AgregarEmpleadoConImagen()`, `addEmpleadoInternal(boolean)` → `agregarEmpleado(boolean)`
- `ModificarEmpleado()`, `ModificarEmpleadoConImagen()`, `modifyEmpleadoInternal(boolean)` → `modificarEmpleado(boolean)`
- `EliminarEmpleado()` → `eliminarEmpleado()`
- `PerfilEmpleado()` → `cargarPerfilEnFormulario()`
- `Filtro()` → `setupFiltros()`
- `LimpiarPerfil()` → `limpiarFormulario()`
- `safeText()`, `safeCombo()` (helpers privados)
- `fillComboBoxPais/Estado/Ciudad/Genero/TipoUsuario/Pago/Banco/PeriodoPago/Contrato()` → `cargarComboBoxes()`

#### Sub-paso 4.2 — Crear `HistorialController`
**Nuevo archivo:** `src/controllers/HistorialController.java`

Mover desde `DashboardController`:
- Campos `@FXML` de historial (tabla historial, filtros de fecha, búsqueda por código)
- `UpdateHistorial()` → `updateHistorial()`
- `BuscarEmpleadoHistorial()` → `buscarEmpleado()`
- `ImprimirReporte()` → `imprimirReporte()`
- `UpdateFechaHistorial()` → `initFechas()`
- `UpdateMesHistorial()` → `updateMesHistorial()`
- `TableProduccionS()` (listener de tabla historial) → `setupTablaHistorial()`

#### Sub-paso 4.3 — Expandir `ProduccionController`
**Archivo existente:** `src/controllers/ProduccionController.java`

Mover desde `DashboardController`:
- Campos `@FXML` de producción (tabla semanal, formularios de agregar/editar producción, perfil empleado en producción)
- `AgregarProduccion()` → `agregarProduccion()`
- `UpdateProduccionSemanal()` → `updateProduccionSemanal()`
- `BuscarEmpleadoProduccion()` → `buscarEmpleado()`
- `BuscarEmpleadoConBotonProduccion()` → `buscarEmpleadoDesdeLista()`
- `PerfilEmpleadoProduccion()` → `cargarPerfilEnFormulario()`
- `cleanProduccion()` → `limpiarFormulario()`
- `FechaActualProduccion()` → `initFecha()`
- `DiasSemana()` → `setupSemanal()`
- `fillComboBoxMaterial/Altura/Calibre/Rombo()` → integrar en `cargarComboBoxes()`

#### Sub-paso 4.4 — Crear `MaterialesController`
**Nuevo archivo:** `src/controllers/MaterialesController.java`

Mover desde `DashboardController`:
- Campos `@FXML` de tablas de Materiales, Alturas, Calibres, Rombos y sus formularios de edición
- `TableMateriales()`, `TableAltura()`, `TableCalibre()`, `TableRombos()` → `setupTables()`
- `UpdateMateriales()`, `UpdateAlturas()`, `UpdateCalibres()`, `UpdateRombos()` → `updateAll()`
- Todos los `CodigoMaterial/Altura/Calibres/Rombos()` → `initCodigos()`
- Todos los métodos `AgregarMaterial*`, `EliminarMaterial*`, `ModificarMaterial*` etc.

#### Sub-paso 4.5 — Crear `PerfilController`
**Nuevo archivo:** `src/controllers/PerfilController.java`

Mover desde `DashboardController`:
- Campos `@FXML` de la sección de perfil y cambio de contraseña
- `Perfil()` → `cargarPerfil()`
- `CambiarContraseña()` → `cambiarContrasena()`
- `UpdateContraseña()` → `guardarNuevaContrasena()`
- `ComprobarPrimerSesion()` → `comprobarPrimerSesion()`
- `CambiarContraseñaPrimera()` → `forzarCambioContrasena()`

#### Sub-paso 4.6 — Crear helpers reutilizables

**`controllers/helpers/ComboBoxLoader.java`** (nuevo):
```java
public class ComboBoxLoader {
    public static void load(ComboBox<String> comboBox,
                            ObservableList<String> options,
                            Supplier<List<String>> dataSource) {
        List<String> data = dataSource.get();
        options.setAll(data);
        comboBox.setItems(options);
    }
}
```

**`controllers/helpers/TableSetup.java`** (nuevo):
Utilidad para configurar `PropertyValueFactory` de `TableColumn`s evitando la repetición de 40 líneas de `setCellValueFactory` en cada `Update*Table()`.

**`controllers/helpers/UiHelpers.java`** (nuevo):
Hay al menos 15 bloques casi idénticos de construcción de `Alert` dispersos en el dashboard. Centralizar en un helper estático elimina esa duplicación:
```java
public final class UiHelpers {
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean showConfirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert.showAndWait().filter(r -> r == ButtonType.OK).isPresent();
    }
}
```

#### Sub-paso 4.7 — Mover operaciones bloqueantes a background `Task`
**Problema:** El método `initialize()` actual realiza ~25 llamadas a la BD en el **hilo de la UI (FX Application Thread)**. Esto congela la ventana durante la carga inicial mientras se consultan usuarios, materiales, alturas, calibres, rombos, producción e historial de forma secuencial y síncrona.

**Solución:** Usar `javafx.concurrent.Task` para las cargas iniciales costosas:
```java
// Ejemplo: carga del dashboard en background
Task<Void> initTask = new Task<>() {
    @Override
    protected Void call() throws Exception {
        // Estas llamadas ya NO bloquean el hilo de UI
        Platform.runLater(() -> updateTable());
        Platform.runLater(() -> updateMateriales());
        Platform.runLater(() -> updateProduccionSemanal());
        return null;
    }
};
new Thread(initTask).start();
```

Operaciones candidatas a ejecutar en background:
- Carga inicial de todas las tablas (`UpdateTable`, `UpdateMateriales`, `UpdateAlturas`, etc.)
- Generación del reporte JasperReports (`ImprimirReporte`) — es la operación más lenta
- Búsqueda de empleado con imagen de perfil (involucra lectura de BLOB)

**`DashboardController` resultante (~200 líneas):**
Solo debe contener:
- Campos `@FXML` de los botones del menú lateral y los `Pane`s de navegación
- Instancias de los sub-controladores
- Un `NavigationController` o método `showPane(Pane target)` que centralice toda la lógica de show/hide de paneles — actualmente ese código está duplicado en cada handler de botón del menú
- `initialize()` que delega a cada sub-controlador su inicialización

**Verificación Fase 4:** Cada sub-paso verifica que la pantalla correspondiente sigue funcionando antes de pasar al siguiente.

---

### FASE 5 — Completar y limpiar la capa de Servicios/DAOs
**Objetivo:** Garantizar que ningún Service tenga SQL directo y que los DAOs no tengan lógica de negocio.  
**Riesgo de regresión:** Bajo  
**Tiempo estimado:** 3–5 horas

#### Paso 5.1 — Añadir métodos faltantes a `UsuariosDao`
```java
// Nuevos métodos en UsuariosDao
public static boolean authenticate(String userId, String password) throws SQLException { ... }
public static boolean isFirstSession(String userId) throws SQLException { ... }
public static boolean verifyPassword(String userId, String password) throws SQLException { ... }
public static boolean changePassword(String userId, String newPassword) throws SQLException { ... }
public static boolean delete(String userId) throws SQLException { ... }
public static int getNextId() throws SQLException { ... }
```

#### Paso 5.2 — Actualizar `UsuariosService` para delegar
```java
// ANTES (SQL directo en Service)
public static boolean isFirstSession(String usuario) {
    String sql = "select pimera_sesion from usuarios ...";
    try (Connection con = ConnectionUtil.getConnection(); ...) { ... }
}

// DESPUÉS (delega al DAO)
public static boolean isFirstSession(String usuario) {
    try {
        return UsuariosDao.isFirstSession(usuario);
    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Error checking first session", ex);
        return false;
    }
}
```

#### Paso 5.3 — Crear `ReportService`
**Nuevo archivo:** `src/services/ReportService.java`

Encapsular toda la lógica de JasperReports (actualmente en `ImprimirReporte()`):
```java
public class ReportService {
    public static void printProduccionReport(String autorId, LocalDate desde, LocalDate hasta) 
            throws JRException, SQLException { ... }
}
```

#### Paso 5.4 — Mover cálculo de edad fuera de `UsuariosDao`
El cálculo `Period.between(dob, LocalDate.now()).getYears()` en `UsuariosDao.save()` es lógica de negocio/dominio, no de persistencia. Moverlo a `DateUtils.calcularEdad(String fechaNacimiento)`.

#### Paso 5.5 — Crear `ValidationUtils`
**Nuevo archivo:** `src/util/ValidationUtils.java`

```java
public final class ValidationUtils {
    // Reemplaza el patrón: if (field.isEmpty()) { value = "NULL"; }
    public static String requiredText(String value, String fieldName) throws ValidationException { ... }
    public static String optionalText(String value) { return (value == null || value.isBlank()) ? null : value; }
    public static boolean isValidEmail(String email) { ... }
    public static boolean isValidCurp(String curp) { ... }
}
```

**Verificación Fase 5:** Los servicios no importan `java.sql.*`. Los DAOs no calculan ni validan datos de negocio.

---

### FASE 6 — Testing y Herramientas de Calidad
**Objetivo:** Añadir cobertura de pruebas para proteger las refactorizaciones anteriores.  
**Riesgo de regresión:** Sin riesgo (solo agrega código)  
**Tiempo estimado:** 1–3 días

#### Paso 6.1 — Migrar a Maven (recomendado)
El build con Ant/NetBeans dificulta la gestión de dependencias y JUnit. Crear `pom.xml` con dependencias declaradas:
- `javafx-controls`, `javafx-fxml`
- `mysql-connector-java`
- `jasperreports`
- `animatefx`
- `junit-jupiter` (5.x), `mockito-core` (para tests)
- `h2` (BD en memoria para tests de integración)

#### Paso 6.2 — Tests unitarios para DAOs con H2
```java
@Test
void testFindById_returnsUser() throws SQLException {
    // Usar H2 en memoria con schema creado desde script SQL
    UsuarioDetalle u = UsuariosDao.findById("testuser");
    assertNotNull(u);
    assertEquals("Juan", u.getNombre());
}
```

#### Paso 6.3 — Tests unitarios para `AuthService`
```java
@Test void loginSuccess() { ... }
@Test void loginInvalidCredentials_returnsINVALID() { ... }
@Test void loginEmptyFields_returnsEMPTY() { ... }
```

#### Paso 6.4 — Tests para `ValidationUtils` y `DateUtils`
```java
@Test void formatLongDate_returnsFormattedString() { ... }
@Test void calcularEdad_returnsCorrectAge() { ... }
```

#### Paso 6.5 — Añadir análisis estático de código
Con Maven ya configurado, añadir al `pom.xml`:
- **Checkstyle** — verifica estilo y convenciones (naming camelCase, imports no usados, etc.).
- **SpotBugs** — detecta bugs en bytecode: null dereferences, recursos no cerrados, comparaciones con `==`.
- **JaCoCo** — genera reporte de cobertura de tests.

```xml
<!-- pom.xml — plugins de calidad -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
</plugin>
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>4.8.3.1</version>
</plugin>
```

Ejecutar con `mvn verify` para obtener reportes en `target/site/`.

#### Paso 6.6 — Configurar GitHub Actions / CI (opcional)
```yaml
# .github/workflows/build.yml
- name: Build and test
  run: mvn verify
```

---

## 7. Nueva Estructura de Paquetes Propuesta

```
src/
├── aceros/                          (recursos FXML — sin cambios)
│   ├── dashboard.fxml
│   ├── Login.fxml
│   ├── title_bar.fxml
│   └── ventana_emergente.fxml
│
├── config/
│   └── AppConfig.java               (sin cambios — bien diseñado)
│
├── controllers/                     (solo controladores JavaFX delgados)
│   ├── DashboardController.java     (reducido a ~200 líneas — solo navegación)
│   ├── LoginController.java         (delega a AuthService)
│   ├── EmpleadosController.java     (NUEVO — CRUD empleados)
│   ├── HistorialController.java     (NUEVO — historial + reportes)
│   ├── MaterialesController.java    (NUEVO — materiales, alturas, calibres, rombos)
│   ├── PerfilController.java        (NUEVO — perfil + cambio de contraseña)
│   ├── ProduccionController.java    (EXPANDIDO — producción semanal)
│   ├── Title_barController.java     (sin cambios)
│   └── helpers/
│       ├── ProfileBinder.java       (existente)
│       ├── ComboBoxLoader.java      (NUEVO)
│       ├── TableSetup.java          (NUEVO)
│       └── UiHelpers.java           (NUEVO — alerts centralizados)
│
├── dao/                             (solo acceso JDBC → retorna modelos)
│   ├── UsuariosDao.java             (+ métodos: authenticate, delete, isFirstSession, etc.)
│   ├── ProduccionDao.java           (sin cambios estructurales)
│   ├── HistorialDao.java            (sin cambios estructurales)
│   ├── MaterialesDao.java
│   ├── AlturasDao.java
│   ├── CalibresDao.java
│   ├── RombosDao.java
│   └── LookupDao.java
│
├── database/
│   └── ConnectionUtil.java          (sin cambios)
│
├── models/                          (TODOS los POJOs y view-models)
│   ├── UsuarioDetalle.java          (existente)
│   ├── Empleados.java               (MOVIDO desde controllers)
│   ├── Materiales.java              (MOVIDO)
│   ├── Historial.java               (MOVIDO)
│   ├── ProduccionSemanal.java       (MOVIDO)
│   ├── Alturas.java                 (MOVIDO)
│   ├── Rombos.java                  (MOVIDO)
│   └── Calibres.java                (MOVIDO)
│
├── services/                        (lógica de negocio — sin SQL directo)
│   ├── AuthService.java             (NUEVO — login, logout)
│   ├── SessionManager.java          (NUEVO — reemplaza static sesion)
│   ├── ReportService.java           (NUEVO — JasperReports)
│   ├── UsuariosService.java         (actualizado — delega a DAO)
│   ├── LookupService.java
│   ├── ProduccionService.java
│   ├── HistorialService.java
│   ├── MaterialesService.java
│   ├── AlturasService.java
│   ├── CalibresService.java
│   └── RombosService.java
│
├── exceptions/                      (NUEVO — excepciones específicas de dominio)
│   ├── AuthException.java           (login fallido, sesión inválida)
│   ├── DaoException.java            (wraps SQLException con contexto)
│   └── ValidationException.java     (errores de validación de formulario)
│
└── util/
    ├── DateUtils.java               (+ calcularEdad(), mesRangeOf())
    ├── ImageUtils.java              (corregido — sin archivos temporales)
    └── ValidationUtils.java         (NUEVO)
```

---

## 8. Criterios de Aceptación

Al finalizar la refactorización completa, el proyecto debe cumplir:

- [ ] `DashboardController.java` tiene menos de 300 líneas.
- [ ] Ningún controlador (`controllers/`) importa `java.sql.*`.
- [ ] Ningún DAO (`dao/`) importa clases de `controllers/`.
- [ ] Ningún `Service` ejecuta SQL directamente (todo delega al DAO correspondiente).
- [ ] No existe ningún `private static Connection con` ni `Connection` como campo de instancia en ningún controlador.
- [ ] Todos los recursos JDBC están dentro de bloques `try-with-resources`.
- [ ] No hay bloques `catch` vacíos en ningún archivo.
- [ ] No hay `System.out.println` en código de producción.
- [ ] Todas las comparaciones de `String` usan `.equals()` o `Objects.equals()`.
- [ ] No se crean archivos temporales para visualizar imágenes.
- [ ] La query de `ImprimirReporte()` usa `PreparedStatement` o parámetros de JasperReports.
- [ ] El estado de sesión está en `SessionManager`, no en campos `static` de controladores.
- [ ] Los modelos de datos están en `models/`, no en `controllers/`.
- [ ] Todas las colecciones tienen tipo genérico declarado (no raw types).
- [ ] Tests unitarios pasan para: `AuthService`, `UsuariosDao`, `ValidationUtils`, `DateUtils`.
- [ ] Las operaciones de carga inicial del dashboard no bloquean el hilo de la UI (usar `Task` para cargas pesadas).
- [ ] No hay bloques de construcción de `Alert` duplicados — todos pasan por `UiHelpers`.
- [ ] SpotBugs no reporta bugs de categoría CORRECTNESS o SECURITY.
- [ ] El comportamiento funcional es idéntico al original (login, CRUDs, reportes, imágenes de perfil).
