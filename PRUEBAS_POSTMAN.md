# Comprobaciones cortas

Usa una copia de tu base y dos cuentas de prueba. Configura Basic Auth y ejecuta Obtener CSRF antes de las escrituras, según LEEME_PRIMERO.md. Ajusta los IDs de la colección; no se rellenan automáticamente. Para modificar catálogos necesitas rol ADMIN.

1. Consulta /api/usuarios/1 y confirma que nunca devuelve passwordHash.
2. Envía onboarding con equipo y vuelve a consultar: debe mostrarlo como texto.
3. Crea un favorito: 200. Repite usuario+ejercicio: 409. Elimina el favorito por su idFavorito: 204.
4. Crea dos programaciones de la misma semana. Marca una completada: resumen 50 %. Cancela la otra: resumen 100 %, cancelados=1.
5. Crea una programación cancelada: no debe aumentar el denominador del porcentaje. Sin programaciones activas: 0 %.
6. Crea un registro con 3 series, 10 repeticiones y 20 kg: 200. Consultarlo en el historial debe conservar esos valores.
7. Envía series=0, pesoKg=-1 o fecha futura: 400. Corrige cantidades con PUT e idRegistro: 200.
8. Renombra el ejercicio: el registro histórico conserva su nombre anterior.
9. Consulta alternativas: mismo grupo muscular y sin el ejercicio original. Si no hay otras opciones devuelve [].
10. Consulta descanso: devuelve el valor de RutinaEjercicio; null significa que falta configurarlo, cero significa sin descanso.
11. Intenta borrar una rutina con registros o programaciones: 409. No se borran automáticamente sus dependencias.
12. Datos válidos con un ID inexistente: 404 en consultas por ID; relaciones inexistentes al crear: 400.

La coincidencia de grupo muscular utiliza la misma búsqueda que el proyecto original; mantén nombres uniformes en el catálogo. El historial está ordenado por fecha, sin agrupar ni calcular gráficas. El frontend puede utilizar sus registros.

Estas comprobaciones son una guía para ejecutar; no se presentan como resultados ya aprobados.


## Security y nuevas historias

1. Sin Basic Auth, consultar un progreso: 401. Con contraseña incorrecta: 401.
2. Autenticado como A, consultar/modificar/eliminar un recurso de B: 403. Revisar perfil, rutina, progreso, favorito, calendario y registro, además de onboarding y consultas por usuario.
3. Los listados generales muestran solo los recursos de A. ADMIN tampoco ve datos personales ajenos.
4. Con rol USER y CSRF válido, modificar el catálogo: 403. Con ADMIN y CSRF: operación permitida si el cuerpo es válido.
5. Sin CSRF, intentar un POST válido: 403. Obtener el token, conservar su cookie y repetir con la cabecera: permitido si los datos son válidos.
6. Comparar un mes con 80 kg y cumplimiento 50 frente a otro con 78 kg y cumplimiento 80: diferencias -2 kg y +30 puntos.
7. Añadir un registro sin peso: no debe reducir el peso promedio. Un cumplimiento de 0 sí debe contarse.
8. Mes vacío, o un mes solo con peso frente a otro solo con cumplimiento: hayDatos=false y mensaje.
9. Meses iguales, 2026-13 o texto sin AAAA-MM: 400. Un registro del primer día del siguiente mes no pertenece al anterior.
10. Resumen con registros desordenados: pesoInicial y pesoFinal se calculan cronológicamente. Exportar: 200, text/csv, Content-Disposition attachment, una fila resumen y sin campos de otro usuario.
11. Sin métricas: resumen 200 hayDatos=false; exportar 422 application/json, sin archivo adjunto.
12. Consulta/exportación de otro usuario: 403.

Los POST de Progreso ponen la fecha actual como en la versión recibida. Para probar varios meses sin esperar, usa registros históricos existentes o modifica la fecha de dos registros de prueba en una copia de la base, identificándolos por id_progreso e id_usuario. No cambies fechas de datos reales.
