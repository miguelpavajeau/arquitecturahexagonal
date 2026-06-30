# PowerUp · Frontend (Astro)

Interfaz web para la API de la plazoleta de comidas **PowerUp** (Spring Boot, arquitectura hexagonal).

## Requisitos

- Node.js `>= 22.12.0`
- El backend corriendo en `http://localhost:8081`

## Configuración

La URL del backend se define en `.env`:

```sh
PUBLIC_API_BASE_URL=http://localhost:8081
```

> El backend solo permite CORS desde `http://localhost:4321`, que es el puerto por defecto del servidor de desarrollo de Astro. Si cambias el puerto, actualiza también el CORS en `SecurityConfiguration.java`.

## Comandos

| Comando           | Acción                                       |
| :---------------- | :------------------------------------------- |
| `npm install`     | Instala las dependencias                     |
| `npm run dev`     | Servidor de desarrollo en `localhost:4321`   |
| `npm run build`   | Compila el sitio a `./dist/`                 |
| `npm run preview` | Previsualiza la compilación localmente       |

## Acceso

El backend siembra un administrador por defecto:

- **Correo:** `admin@pragma.com`
- **Clave:** `Admin123*`

## Estructura

```text
src/
├── layouts/
│   └── Layout.astro      # Layout compartido, estilos globales y barra de navegación por rol
├── lib/
│   └── api.js            # Cliente HTTP, manejo de token y guardas de sesión
└── pages/
    ├── index.astro       # Login
    ├── dashboard.astro   # Inicio con accesos según el rol
    ├── usuarios.astro    # (ADMINISTRADOR) Crear propietarios y listar usuarios
    ├── restaurantes.astro# (ADMINISTRADOR) Crear restaurantes
    ├── categorias.astro  # Crear y listar categorías
    └── platos.astro      # (PROPIETARIO) Crear y actualizar platos
```

## Roles y permisos

| Acción              | Rol requerido   |
| :------------------ | :-------------- |
| Crear propietario   | `ADMINISTRADOR` |
| Crear restaurante   | `ADMINISTRADOR` |
| Crear/actualizar plato | `PROPIETARIO` |
| Categorías          | Autenticado     |

La navegación y las páginas se adaptan al rol contenido en el JWT; el acceso real lo valida el backend.
