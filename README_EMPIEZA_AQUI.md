# TrueForm — API REST SIN Spring Security (solo desarrollo local)

Esta es una **versión de práctica** del backend original `TrueForm_API_security`, no una API segura para publicación.

## Qué cambió
- Se quitó `spring-boot-starter-security`, la configuración `SecurityConfig`, el filtro HTTP Basic y el endpoint CSRF.
- También se retiraron las verificaciones de `AccesoServicio` de los servicios. Las rutas ya no exigen autorización.
- Los `listar()` que dependían del «usuario actual» ahora consultan todos los registros. Para filtrar por usuario, utiliza las rutas `.../usuario/{idUsuario}` ya existentes.
- Se conserva `POST /api/auth/login` para **validar la contraseña y mostrar una pantalla de demostración**, pero **no autentica ni crea sesión y no protege nada**.
- El registro conserva contraseñas cifradas con BCrypt; para ello se conserva solamente `spring-security-crypto` (sin filtros de seguridad ni JWT).
- Se mantiene la estructura Controller -> Service -> Repository -> PostgreSQL y las demás operaciones CRUD.

## Cómo abrir en IntelliJ IDEA
1. Abre **esta carpeta**, que contiene `pom.xml` (no la carpeta ZIP externa).
2. JDK **21**. Maven puede importar las dependencias automáticamente.
3. Crea o comprueba en PostgreSQL la base `db_trueform` (la API original ya usaba esta base).
4. Ajusta la contraseña en la variable de entorno `DB_PASSWORD` de la configuración de ejecución de IntelliJ (Run -> Edit Configurations -> Environment variables), o configúrala localmente sin subirla a Git.
5. Ejecuta `src/main/java/com/trueform/backend/Grupo2Application.java`.
6. Abre `http://localhost:8080/swagger-ui/index.html` o comprueba `http://localhost:8080/api/objetivos`.

## Para probar con Angular
- Angular suele usar `http://localhost:4200` y esta API `http://localhost:8080`.
- En el proyecto Angular anterior, sustituye `src/app/servicios/trueform-api.service.ts` por el archivo de **PARCHE_OPCIONAL_ANGULAR/**. Ese frontend anterior llamaba `/api/auth/csrf`, que en esta versión dejó de existir.
- Deja configurado `src/proxy.conf.json` y ejecuta `npm start` desde la carpeta de Angular que contiene `package.json`.

## Importante: protección y privacidad
**NO publicar esta versión en internet ni desplegarla en servidores compartidos.** Todos los endpoints, incluyendo registros de usuarios y cambios en datos, son accesibles sin autenticación. El backend se liga a `127.0.0.1` intencionalmente, solo en la máquina local. Usa **datos de prueba**, nunca datos reales de usuarios. Para producción será necesario reintroducir autenticación, autorización y protección CSRF donde corresponda.

El login solo comprueba credenciales y no garantiza identidad en las siguientes solicitudes; el `localStorage` del frontend no equivale a una sesión real.

Consulta `CAMBIOS_SIN_SECURITY.md` y `PRUEBAS_POSTMAN.md`.
