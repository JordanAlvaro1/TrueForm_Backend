# Pruebas en Postman — sin Security

Arranca Spring Boot con PostgreSQL disponible. **Sin pestaña Authorization y sin token CSRF.**

1. `GET http://localhost:8080/api/objetivos` — lista de objetivos (puede estar vacía).
2. `GET http://localhost:8080/api/ejercicios` — lista de ejercicios (puede estar vacía).
3. `GET http://localhost:8080/api/usuarios` — lista usuarios de **prueba**; no requiere inicio de sesión.
4. `GET http://localhost:8080/api/rutinas` — lista de todas las rutinas. Para filtrar usa las rutas por usuario que ya existen.
5. `POST http://localhost:8080/api/auth/login` con `Content-Type: application/json` y `{"email":"correo-de-prueba@example.com","password":"clave-de-prueba"}`. Si el usuario existe y la contraseña coincide, devuelve UsuarioDTO; de lo contrario `401`. Este login **no** habilita permisos; todas las rutas están abiertas por ser una versión de demostración.

Para probar los métodos POST, PUT o DELETE de otros controladores, verifica los DTO y evita modificar datos reales.

**Si falla:** `401` solo es esperable en `/api/auth/login` por credenciales incorrectas. Si otras rutas fallan con `500`, revisa la consola de IntelliJ y la conexión a PostgreSQL. `404` significa ruta inexistente o dato no encontrado, según el endpoint.
