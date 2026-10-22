# LimpiezaIT API

API REST de productos de limpieza, proyecto integrador del curso **Java Backend** de educaciónIT.

## Stack

- Java 21
- Spring Boot 4.1
- Maven
- Spring Data JPA + H2 (base en memoria: se borra y se vuelve a cargar en cada arranque)
- Spring Security con JWT (librería JJWT)

Más adelante: springdoc/Swagger.

## Cómo correrlo

**Desde Eclipse:** clic derecho sobre el proyecto → *Run As → Spring Boot App*.

**Desde la terminal:**

```bash
./mvnw spring-boot:run
```

URL base: http://localhost:8083

Prueba rápida:

```bash
curl http://localhost:8083/api/ping
# {"app":"LimpiezaIT","estado":"ok"}
```

## Endpoints

| Método | URL | Descripción | Rol requerido |
|---|---|---|---|
| `GET` | `/api/ping` | Verifica que la API esté levantada | — |
| `POST` | `/auth/login` | Devuelve un token JWT (401 si las credenciales son incorrectas) | — |
| `GET` | `/api/productos` | Lista todos los productos | USER o ADMIN |
| `GET` | `/api/productos/{id}` | Busca un producto por id (404 si no existe) | USER o ADMIN |
| `POST` | `/api/productos` | Crea un producto (201) | ADMIN |
| `PUT` | `/api/productos/{id}` | Modifica un producto (404 si no existe) | ADMIN |
| `DELETE` | `/api/productos/{id}` | Borra un producto (204, o 404 si no existe) | ADMIN |
| `GET` | `/api/usuarios/me` | Muestra el usuario y los roles del token | USER o ADMIN |
| `GET` | `/api/usuarios` | Lista los usuarios | ADMIN |

Al arrancar se cargan 2 categorías (1 = Pisos, 2 = Baño), 3 productos y 2 usuarios de prueba (`config/DataSeeder.java`).

## Seguridad (JWT)

Todo `/api/**` pide un token, salvo `/api/ping`. El flujo es:

1. `POST /auth/login` con usuario y contraseña → la API devuelve un **token JWT**.
2. En cada request siguiente se manda el header `Authorization: Bearer <token>`.
3. `JwtAuthFilter` valida el token (firma y vencimiento) y carga el usuario con sus roles.

Los usuarios se guardan en la tabla `usuarios` con la contraseña en BCrypt, y se cargan con `security/CustomUserDetailsService.java`:

| Usuario | Contraseña | Rol | Puede |
|---|---|---|---|
| `user` | `user123` | USER | Consultar (GET) |
| `admin` | `admin123` | ADMIN | Consultar, crear, modificar y borrar; listar usuarios |

```bash
# 1. Login
curl -X POST http://localhost:8083/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}'
# {"token":"eyJhbGciOiJIUzM4NCJ9...","tipo":"Bearer"}

# 2. Usar el token
curl http://localhost:8083/api/productos -H "Authorization: Bearer eyJhbGciOiJIUzM4NCJ9..."
```

| Código | Significa |
|---|---|
| **401 Unauthorized** | No mandaste token, es inválido o venció ("no sé quién sos"). En el login: usuario o contraseña incorrectos |
| **403 Forbidden** | El token es válido, pero tu rol no alcanza ("sé quién sos, pero no podés") |

Configuración en `application.properties`:

- `jwt.secret`: clave en Base64 con la que se firman los tokens. Se toma de la variable de entorno `JWT_SECRET`; el valor por defecto del archivo es **solo para el curso**: en producción no se deja una clave en el repositorio.
- `jwt.expiracion-ms`: duración del token, 1800000 ms = 30 minutos. Al vencer, hay que volver a hacer login.

Los permisos se definen por URL en `config/SecurityConfig.java` y también por método con `@PreAuthorize` (ver `UsuarioController`). Para que `@PreAuthorize` funcione, `SecurityConfig` necesita `@EnableMethodSecurity`: sin esa anotación Spring la ignora sin avisar.

La consola web de H2 está desactivada desde la clase 4 (`spring.h2.console.enabled=false`).

## Probar con Postman

La colección está en `postman/LimpiezaIT.postman_collection.json` y trae todos los requests listos (los que mandan datos ya tienen el JSON cargado en el body).

### Importar la colección

1. Abrir Postman y tocar **Import** (arriba a la izquierda).
2. Arrastrar el archivo `postman/LimpiezaIT.postman_collection.json`, o tocar *files* y buscarlo dentro de la carpeta del proyecto.
   - Si no clonaste el repo, también se puede pegar esta URL en el cuadro de Import:
     `https://raw.githubusercontent.com/fertw/java-backend-2026/main/postman/LimpiezaIT.postman_collection.json`
3. Confirmar con **Import**. Aparece la colección **LimpiezaIT** en el panel izquierdo.

### Puerto

La URL base está en la variable `baseUrl` de la colección (`http://localhost:8083`). Si tu API corre en otro puerto: clic en la colección → pestaña **Variables** → cambiar `baseUrl` → **Save**.

### Login y token

1. Correr **Auth → Login admin** (o **Login user**). Un script del request guarda el token en la variable `token` de la colección.
2. Los demás requests lo mandan solos: la colección tiene *Authorization* → **Bearer Token** con `{{token}}`, y los requests están en *Inherit auth from parent*.
3. Para cambiar de usuario, correr el otro login. Cuando el token vence (30 minutos) los requests dan 401: volver a hacer login.

Si armás un request desde cero: pestaña **Authorization** → *Type:* **Bearer Token** → pegar el token, **sin** escribir `Bearer` adelante (Postman lo agrega). Escribiendo el header a mano en **Headers**: `Authorization` = `Bearer eyJ...`, con un espacio entre `Bearer` y el token.

El login es `POST {{baseUrl}}/auth/login` (sin `/api`), con body **raw → JSON**:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

### Enviar JSON a mano

Si armás un request desde cero (por ejemplo, un `POST` para crear un producto):

1. Pestaña **Body** → **raw**.
2. En el desplegable de la derecha cambiar *Text* por **JSON**.
3. Pegar el JSON y tocar **Send**:

```json
{
  "nombre": "Lavandina 1L",
  "precio": 1500,
  "descripcion": "Uso general",
  "url_foto": "https://img.limpiezait.com/lavandina.png",
  "categoria_id": 2
}
```

Los nombres del JSON los define `ProductoDTO`: se escribe `url_foto` y `categoria_id`, no `urlFoto`. No se mandan `id`, `fecha_alta` ni `categoria`: los completa la API. La respuesta los trae:

```json
{
  "id": 4,
  "nombre": "Lavandina 1L",
  "precio": 1500.0,
  "descripcion": "Uso general",
  "url_foto": "https://img.limpiezait.com/lavandina.png",
  "fecha_alta": "20/10/2026 19:32",
  "categoria_id": 2,
  "categoria": "Baño"
}
```

Los campos en `null` no aparecen en la respuesta.

| Problema | Causa |
|---|---|
| **415 Unsupported Media Type** | El body quedó en *Text*, no en **JSON** |
| **401 Unauthorized** | No hiciste login, el token venció o quedó `Bearer Bearer ...` en el header. En el login: la contraseña está mal o mandaste `usuario` en vez de `username` |
| **405 Method Not Allowed** en el login | Lo mandaste como `GET`: es `POST` |
| **403 Forbidden** | Usaste el token de `user` para un POST, PUT, DELETE o para listar usuarios: hace falta `admin` |
| Un campo llega en `null` | Su nombre no coincide con el del DTO (por ejemplo, `urlFoto` en vez de `url_foto`) |
| **404** "Categoría N no encontrada" | El `categoria_id` no existe |
| `Could not send request` | La API no está levantada o el puerto de `baseUrl` es otro |

## Estructura del repositorio

```
limpiezait-api/
├── src/        código fuente y tests
├── postman/    colección y environment de Postman exportados
├── docs/       openapi.yaml (clase 6), diagramas, notas
├── pom.xml
└── README.md
```

## Recorrido del curso

Al terminar cada clase se crea una etiqueta sobre el último commit:

| Etiqueta | Lo que agrega |
|---|---|
| `clase-1` | CRUD de productos en memoria |
| `clase-2` | Arquitectura en capas + manejo de errores |
| `clase-3` | JPA + H2, Categorías, DTOs y mappers |
| `clase-4` | Spring Security con Basic Auth y roles |
| `clase-5` | Login con JWT y usuarios en base de datos |
| `v1.0` | Documentación con Swagger/OpenAPI |

## Cómo ponerte al día si te atrasaste

Para arrancar la clase N desde donde terminó la clase anterior:

```bash
git fetch --tags
git checkout -b mi-clase-N clase-(N-1)
```

Por ejemplo, para arrancar la clase 3: `git checkout -b mi-clase-3 clase-2`.

## Convención de commits

Usamos [Conventional Commits](https://www.conventionalcommits.org/es/), en español y en imperativo:

```
tipo(alcance): descripción corta
```

| Tipo | Cuándo usarlo |
|---|---|
| `feat` | Funcionalidad nueva (endpoint, entidad, servicio) |
| `refactor` | Cambio de estructura sin cambiar comportamiento (ej. pasar a capas) |
| `fix` | Corrección de un error |
| `test` | Tests |
| `docs` | README, `docs/`, colección Postman |
| `chore` | Configuración, dependencias, `pom.xml`, `.gitignore` |

**Alcance:** la capa o el tema: `model`, `controller`, `service`, `repository`, `exception`, `config`, `security`, `dto`, `mapper`, `postman`.

Ejemplos:

```
feat(model): agregar entidad Producto
feat(controller): CRUD de productos en memoria
refactor(service): mover lógica de negocio a ProductoService
feat(exception): manejo global de errores con @RestControllerAdvice
chore(pom): agregar Spring Data JPA y H2
docs(postman): exportar colección clase 2
```

Regla: **un commit por paso de la guía de la clase**. Hacer varios commits por clase está bien.

### Etiquetas y ramas

- Al terminar cada clase, se crea una etiqueta **anotada** sobre el último commit:
  ```bash
  git tag -a clase-N -m "Fin clase N: <resumen>"
  ```
- Al final del curso se crea la etiqueta `v1.0`.
- Se trabaja sobre una sola rama, `main`, y no se crean ramas por clase.
- Los experimentos o desafíos van en ramas `desafio/<nombre>`, que no se mergean a `main` sin probarlas.
