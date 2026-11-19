# Cambios realizados respecto a TrueForm_API_security original

- Eliminado `config/SecurityConfig.java`.
- Eliminado `config/UsuarioAutenticado.java`.
- Eliminado `servicios/AccesoServicio.java`.
- Eliminado `controladores/CsrfController.java`.
- Eliminadas 45 llamadas de verificación de acceso por usuario o rutina.
- Eliminada la dependencia `spring-boot-starter-security` y `spring-security-test`.
- Conservado solo `spring-security-crypto` para BCrypt de registro y login (no implementa seguridad HTTP).
- Actualizadas listas generales de usuarios, rutinas, rutinas-ejercicios y progresos para usar `findAll()`.
- CORS habilitado para localhost:4200 y localhost:5500 durante desarrollo.
- El backend escucha solo en `127.0.0.1:8080`.
- La contraseña de PostgreSQL pasó a `${DB_PASSWORD:}` para no distribuir credenciales locales.
- La clave de IA es opcional en la configuración; la llamada IA puede fallar si no está disponible.
- Sustituidas las pruebas que exigían filtros de Security por pruebas de listados sin autenticación.
- Incluido un parche opcional para el servicio Angular, eliminando la consulta previa a CSRF.

**ATENCIÓN**: Esta versión es solo para actividades de clase y datos de prueba. Todas las operaciones quedan sin control de permisos.
