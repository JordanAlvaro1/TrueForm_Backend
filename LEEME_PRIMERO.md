# TrueForm: Security + US-SA-15 + US-SA-17

Esta actualización parte del último TrueForm_API.zip recibido. Conserva sus controladores, servicios, entidades y funciones del equipo, incluida IA, tips, glosario, rutina guiada, recomendaciones e historial.

## Abrir y ejecutar

1. Abre `Grupo2` como proyecto Maven con **JDK 21**. Se mantienen Spring Boot 4.0.8 y las versiones originales.
2. Conserva tu configuración local de PostgreSQL en `src/main/resources/application.properties` y configura la clave de Gemini si usarás IA.
3. Recarga Maven. Se agregó `spring-boot-starter-security` en lugar de la dependencia aislada de crypto; BCrypt sigue disponible. `spring-security-test` solo se usa en pruebas.
4. Ejecuta `Grupo2Application`. Con `ddl-auto=update`, Hibernate añade la columna nullable `usuarios.rol`. No es necesario borrar tablas ni registros.
5. Importa `TrueForm_Sencillo.postman_collection.json`. El nombre se conserva para actualizar la colección anterior.

## Security

- Autenticación **HTTP Basic**, usando email y contraseña original; BCrypt verifica el hash existente. No envíes el hash como contraseña. Para un servidor remoto usa HTTPS.
- Cada petición protegida debe llevar Basic Auth. `POST /api/auth/login` sigue comprobando las credenciales y devolviendo UsuarioDTO, pero no genera JWT ni autentica las peticiones siguientes.
- Los usuarios solo pueden consultar y modificar sus propios datos: perfil, onboarding, rutinas, ejercicios de rutina, progreso, favoritos, calendario, registros y consultas personalizadas. Cambiar el ID no otorga acceso.
- Los listados generales de usuarios, rutinas, progreso y ejercicios de rutina ahora muestran solo los datos del usuario autenticado.
- El catálogo de objetivos se puede leer sin autenticación para permitir el registro. El catálogo de ejercicios requiere autenticación. Solo ADMIN puede crear, editar o borrar ambos catálogos.
- El registro público siempre asigna USER; los usuarios anteriores con rol null también se consideran USER. Ningún DTO permite asignarse ADMIN. Un administrador tampoco puede consultar datos personales ajenos.
- Registro, login, consulta del token CSRF y documentación Swagger son públicos. Públicos significa que no exigen Basic; los POST siguen exigiendo CSRF.
- Se conserva CSRF: primero consulta `GET /api/auth/csrf`, conserva la cookie JSESSIONID y envía el valor `token` en la cabecera que indica `headerName` (normalmente X-CSRF-TOKEN) en POST, PUT y DELETE. Este token protege escrituras; no sustituye la contraseña ni es JWT. GET no necesita token CSRF.

Para asignar el administrador del catálogo, el responsable de la base puede ejecutar (sustituye el correo):

```sql
UPDATE usuarios SET rol = 'ADMIN' WHERE lower(email) = lower('correo-del-administrador@example.com');
```

No se crea una cuenta ni una contraseña administrativa predeterminada.

### Postman

Configura `email`, `password`, `idUsuario` e IDs de tus recursos en las variables de la colección. Ejecuta primero **Obtener CSRF**: guarda el token automáticamente. Postman conserva la cookie; el script de la colección incorpora el token en las escrituras. Las peticiones protegidas heredan Basic Auth.

Si la cookie caduca o la eliminas, vuelve a obtener CSRF. Si cambias el email del perfil, actualiza la variable email antes de la siguiente petición. No compartas una colección exportada con contraseñas reales.

Para el frontend existente de localhost:5500, usar `credentials: "include"` al obtener CSRF y al escribir, además de Authorization Basic. Se mantienen únicamente los dos orígenes locales que ya estaban autorizados. Las cookies deben pertenecer al mismo host; no mezcles localhost con 127.0.0.1.

## US-SA-15: comparación mensual

```http
GET /api/progresos/usuario/1/comparacion-mensual?mesBase=2026-08&mesComparado=2026-09
```

Devuelve `meses`, una lista de dos resúmenes con período, registros relevantes, número de mediciones, peso promedio, primer y último peso, cumplimiento promedio y fechas. Incluye diferencias de peso promedio (kg) y cumplimiento (puntos porcentuales), calculadas como segundo mes menos primero.

Las fuentes son los registros de **Progreso**, que ya utiliza el historial físico. No se mezclan con RegistroEntrenamiento ni con el calendario: esas tablas representan cosas diferentes. `registrosProgreso` cuenta registros con peso o cumplimiento, no sesiones únicas. Los promedios omiten campos null y conservan ceros válidos. Una observación sin métricas no basta.

Se requiere al menos una medición de la misma métrica en cada mes. Si no se cumple, HTTP 200 devuelve `hayDatos: false` y un mensaje explicativo; las diferencias son null. Una métrica ausente no se interpreta como cero. No se afirma que bajar de peso sea una mejora: depende del objetivo del usuario.

Los meses deben ser distintos y usar AAAA-MM; entradas inválidas devuelven 400. Se puede comparar cualquier par, incluso de años diferentes. Cada período comprende desde el primer día incluido hasta el primer día del siguiente mes excluido, según las fechas locales ya usadas por la base.

**Las figuras corresponden al frontend.** El API entrega los valores: eje X = `meses[].periodo`; eje Y = `pesoPromedioKg` o `cumplimientoPromedio`. No dibujes null como cero. Usa gráficas separadas porque kg y porcentaje tienen unidades distintas. El criterio visual queda completo cuando la interfaz dibuje esas figuras.

## US-SA-17: resumen y exportación

```http
GET /api/progresos/usuario/1/resumen
GET /api/progresos/usuario/1/exportar
```

`resumen` devuelve JSON con las métricas de todo el historial. `exportar` descarga `progreso-1.csv`, con una cabecera y una fila de resumen, UTF-8 con BOM y separador punto y coma. Incluye período real, cantidades, peso promedio/inicial/final y cumplimiento promedio. Los campos sin mediciones quedan vacíos. No incluye observaciones privadas ni el historial completo.

En Postman usa **Send and Download** para guardar el CSV. Si Excel no separa las columnas automáticamente, importa desde Texto/CSV y selecciona UTF-8, separador `;` y punto como separador decimal.

Sin datos relevantes, `resumen` responde 200 con `hayDatos: false`; `exportar` responde **422 con JSON y un mensaje**, sin adjuntar un CSV vacío. Por eso el frontend debe revisar el estado antes de descargar.

## Respuestas de error

| Caso | Respuesta |
|---|---|
| Consulta protegida sin credenciales o contraseña incorrecta | 401 |
| Recurso ajeno o rol insuficiente | 403 |
| Escritura sin CSRF válido | 403, incluso en registro/login |
| Mes mal formado o repetido | 400 |
| ID inexistente en consulta individual | 404 |
| Conflicto de integridad de las operaciones existentes | 409 |
| Exportación sin métricas | 422 con JSON |
| Comparación sin métricas comunes | 200, hayDatos=false |

## Código a estudiar

- `InformeProgresoController` → `InformeProgresoServicio` → `ProgresoRepositorio`: las dos historias, con DTO, consultas findBy, for, if, BigDecimal y fechas de Java.
- `SecurityConfig`: las rutas públicas/protegidas, Basic y roles.
- `UsuarioAutenticado` conserva el ID autenticado, incluso si se edita el correo.
- `AccesoServicio` y las llamadas añadidas a servicios existentes: comprobación del dueño de los datos.
- `CsrfController`: obtener el token para las escrituras.

Las historias usan la estructura existente. Security incorpora sus clases de configuración necesarias; no se añadieron JWT, gráficos en el backend, librerías PDF, transacciones de negocio nuevas ni una arquitectura diferente. Las pruebas usan JUnit/Mockito, separados del código de la aplicación.

## Verificación y límites

En este entorno pasaron el análisis sintáctico de los 94 archivos Java y 20 comprobaciones aisladas de cálculos, meses, valores faltantes y CSV. Para estas últimas se sustituyeron repositorio/acceso por dobles locales y se generaron los accesores de los DTO; no prueban Spring, JPA, PostgreSQL ni la cadena real de Security.

La compilación Maven se intentó y quedó bloqueada al descargar el parent por un error DNS de repo.maven.apache.org. Además, el entorno tiene Java 17, mientras el proyecto necesita 21. No se afirma que el proyecto completo compile ni que las pruebas JUnit hayan pasado aquí.

Con Java 21 y Maven disponibles:

```bash
./mvnw -Dtest=InformeProgresoTests,InformeProgresoControllerTests,SecurityTests test
./mvnw test
```

En Windows usa `mvnw.cmd`. La primera orden ejecuta las pruebas añadidas sin PostgreSQL; la segunda también ejecuta el test de contexto original, que requiere la base configurada. Sigue además `PRUEBAS_POSTMAN.md` para la comprobación con datos reales.

Referencias de configuración: [Spring Security: autenticación con repositorio](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html) y [protección CSRF](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html).
