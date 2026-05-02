# Aceros y Trefilados — Sistema de Gestión de Personal y Producción

> **Nota de origen y propósito**
>
> Este programa nació como proyecto de una materia universitaria, hace ya algunos años. Su alcance original fue académico: demostrar conceptos de desarrollo de software con Java y JavaFX en un contexto de clase. Nunca estuvo pensado para un entorno de producción real.
>
> Lo que ves en esta rama es una **refactorización total**, hecha desde cero tomando como punto de partida el proyecto original (disponible en la rama [`legacy`](https://github.com/LuisBravoC/aceros/tree/legacy)), realizada como **hobby personal y ejercicio de nostalgia**. Es un tributo a esos momentos de universidad, ahora aplicando buenas prácticas, arquitectura en capas, seguridad básica, pruebas automatizadas y todo lo que con el tiempo uno aprende a hacer bien.
>
> Cualquiera puede usarlo, montarlo y adaptarlo libremente. Solo ten en cuenta que su punto de partida fue un proyecto escolar; si lo llevas a un uso real, tendrás que evaluar los requisitos adicionales (seguridad, escalabilidad, cumplimiento normativo, etc.) que ese contexto exige.

---

## Descripción

Aplicación de escritorio JavaFX para gestionar empleados y producción en una empresa ficticia de aceros. Permite:

- **Gestión de empleados**: alta, baja, modificación, consulta, foto de perfil.
- **Control de producción**: registro semanal de producción por empleado.
- **Historial y reportes**: consulta por rango de fechas y generación de reportes con JasperReports.
- **Catálogos**: materiales, alturas, calibres y separaciones de rombos.
- **Autenticación**: login con hash de contraseña, flujo de primer acceso y cambio de contraseña.
- **Roles**: GERENTE, SUPERVISOR y EMPLEADO GENERAL, cada uno con acceso diferenciado a funciones.

---

## Tecnologías

| Componente | Versión / Detalle |
|---|---|
| Lenguaje | Java 8 (JDK 1.8.0_111) |
| UI | JavaFX 8 (bundled con JDK 8) |
| Build | Apache Ant + NetBeans 8.2 |
| Base de datos | MySQL 5.7 |
| JDBC driver | MySQL Connector/J |
| Reportes | JasperReports |
| Testing | JUnit 4.12 + Hamcrest 1.3 |
| Animaciones | AnimateFX |

---

## Arquitectura

El proyecto sigue una arquitectura en capas con separación de responsabilidades:

```
UI (FXML + Controllers)
        │
    Services          ← lógica de negocio, orquestación
        │
      DAOs            ← acceso a datos, queries JDBC
        │
    Database          ← MySQL vía ConnectionUtil (config externo)
```

```
src/
├── aceros/           → archivos FXML
├── config/           → AppConfig, ConnectionUtil (lee config.properties)
├── controllers/      → DashboardController, LoginController, TitleBarController
│   └── helpers/      → ProfileBinder, CatalogoUtils
├── dao/              → UsuariosDao, ProduccionDao, HistorialDao, LookupDao,
│                         MaterialesDao, AlturasDao, CalibresDao, RombosDao
├── models/           → POJOs: UsuarioDetalle, Usuarios, Empleados,
│                         Materiales, Alturas, Calibres, Rombos,
│                         Historial, ProduccionSemanal
├── services/         → AuthService, SessionManager, UsuariosService,
│                         ProduccionService, HistorialService, LookupService,
│                         MaterialesService, AlturasService, CalibresService,
│                         RombosService, ReporteService
├── util/             → DateUtils, ImageUtils
├── reports/          → report.jrxml (plantilla JasperReports)
├── css/              → hojas de estilo JavaFX
└── icons/            → íconos y sin_perfil.png
test/                 → 79 pruebas unitarias e integración (JUnit 4)
db/
├── aceros_v2.sql     → esquema normalizado + datos de catálogo semilla
└── geo_dump.sql      → datos geográficos (países, estados, ciudades)
config/
└── config.properties → credenciales y URL de BD (NO commitear en producción)
```

---

## Requisitos previos

- JDK 1.8 (Java 8)
- MySQL 5.7
- Apache Ant (incluido en NetBeans 8.2 en `extide/ant/`)
- NetBeans 8.2 (opcional; el build funciona con Ant puro)

---

## Instalación y configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/LuisBravoC/aceros.git
cd aceros
```

### 2. Crear la base de datos

```bash
mysql -u root -p < db/aceros_v2.sql
mysql -u root -p acerosytrefilados < db/geo_dump.sql
```

Esto crea la base `acerosytrefilados` con el esquema normalizado, los catálogos con datos de ejemplo (materiales, calibres, alturas, rombos) y un usuario GERENTE inicial:

| Campo | Valor |
|---|---|
| Usuario ID | *(generado automáticamente)* |
| Nombre | Admin |
| Apellido | Aceros Sistema |
| Contraseña inicial | `1234` |
| Rol | GERENTE |

> Al primer inicio de sesión el sistema obligará a cambiar la contraseña.

### 3. Configurar credenciales de BD

Editar `config/config.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/acerosytrefilados?autoReconnect=true&useSSL=false
db.user=root
db.password=TU_CONTRASEÑA
```

> **Importante:** nunca subas este archivo con contraseñas reales a un repositorio público. Agrégalo a `.gitignore` si lo vas a adaptar para uso propio.

### 4. Compilar

```bash
# En Windows con NetBeans instalado en C:\Program Files\NetBeans 8.2
$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_111"
& "C:\Program Files\NetBeans 8.2\extide\ant\bin\ant.bat" -f build.xml clean compile
```

### 5. Ejecutar

```bash
& "C:\Program Files\NetBeans 8.2\extide\ant\bin\ant.bat" -f build.xml run
```

O abrir el proyecto directamente en NetBeans 8.2 y presionar **Run**.

---

## Ejecutar pruebas

Se requiere una base de datos de pruebas separada. Crear `config/test.config.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/aceros_test?autoReconnect=true&useSSL=false
db.user=root
db.password=TU_CONTRASEÑA
```

Crear la BD de pruebas:

```bash
mysql -u root -p -e "CREATE DATABASE aceros_test;"
mysql -u root -p aceros_test < db/aceros_v2.sql
```

Ejecutar las 79 pruebas:

```bash
& "C:\Program Files\NetBeans 8.2\extide\ant\bin\ant.bat" -f build.xml compile-test test "-Dapp.config=config/test.config.properties"
```

Las pruebas cubren:
- Todos los DAOs (UsuariosDao, ProduccionDao, HistorialDao, LookupDao, MaterialesDao, AlturasDao, CalibresDao, RombosDao)
- Services (AuthService, UsuariosService, LookupService, catálogos)
- SessionManager

---

## Ramas

| Rama | Descripción |
|---|---|
| `main` | Rama principal estable |
| `feature/refactor-fase-1` | Refactorización completa (activa) |
| `legacy` | Proyecto original de universidad, sin modificar |

---

## Qué se mejoró respecto al proyecto original

El código original en `legacy` era funcional para su propósito académico, pero acumulaba las deudas técnicas típicas de un primer proyecto: consultas SQL concatenadas con strings, conexiones sin cerrar, toda la lógica mezclada en los controladores, credenciales hardcodeadas, sin pruebas, y sin separación de capas.

La refactorización corrigió todo eso de forma incremental:

- **Seguridad**: eliminación de concatenaciones SQL — todas las consultas usan `PreparedStatement`. Credenciales externalizadas a `config.properties`.
- **Recursos**: `try-with-resources` en todas las operaciones JDBC para evitar fugas de conexiones.
- **Arquitectura en capas**: separación estricta de UI (controllers), lógica de negocio (services) y acceso a datos (DAOs).
- **Autenticación**: `AuthService` con verificación de contraseña; `SessionManager` singleton que reemplaza el estado global `static`.
- **Normalización de BD**: esquema v2 con claves foráneas para géneros, tipos de pago, bancos, contratos, tipos de usuario, materiales, calibres, alturas y rombos. Consultas con `JOIN` en lugar de valores de texto plano duplicados.
- **Imágenes de perfil**: almacenadas como `BLOB` en BD. Al modificar un empleado sin seleccionar nueva foto, la imagen existente se preserva (no se sobreescribe con el placeholder).
- **Filtros de tabla**: búsqueda en tiempo real por ID, nombre, edad y sueldo con `FilteredList` + `SortedList`.
- **Manejo de errores**: logging con `java.util.logging` en lugar de `System.out.println`; alertas visuales en la UI para errores de usuario.
- **Bug fixes acumulados**: `ArrayIndexOutOfBoundsException` al ordenar columnas, campo `primera_sesion` con tipografía corregida, valores `"NULL"` literales en campos opcionales, doble registro de listeners, entre otros.
- **79 pruebas automatizadas**: cobertura de DAOs y services con base de datos de prueba aislada.

---

## Licencia

Sin licencia formal — el proyecto es de uso libre. Si lo adaptas, conserva una referencia a su origen universitario si lo consideras adecuado.
