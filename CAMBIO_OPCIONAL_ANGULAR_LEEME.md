# Parche opcional para el frontend Angular anterior

1. Abre el proyecto `TrueForm_Angular_WebStorm_CSS_por_componentes` en WebStorm.
2. Sustituye el archivo `src/app/servicios/trueform-api.service.ts` por el de la carpeta `PARCHE_OPCIONAL_ANGULAR`.
3. Deja las demás páginas, componentes y CSS tal como estaban.
4. Ejecuta `npm start` con la API de desarrollo local iniciada.

Esto elimina la solicitud previa a `/api/auth/csrf` del frontend. Sigue siendo un login **solo visual**: todas las rutas del backend están abiertas en modo demostración.
