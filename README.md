# LimpiezaIT API

API REST de productos de limpieza, proyecto integrador del curso **Java Backend** de educaciónIT.

## Stack

- Java 21
- Spring Boot 4.1
- Maven
- Spring Data JPA + H2 (base en memoria: se borra y se vuelve a cargar en cada arranque)
- Spring Security con Basic Auth

Más adelante: JWT, springdoc/Swagger.

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
| `GET` | `/api/productos` | Lista todos los productos | USER o ADMIN |
| `GET` | `/api/productos/{id}` | Busca un producto por id (404 si no existe) | USER o ADMIN |
| `POST` | `/api/productos` | Crea un producto (201) | ADMIN |
| `PUT` | `/api/productos/{id}` | Modifica un producto (404 si no existe) | ADMIN |
| `DELETE` | `/api/productos/{id}` | Borra un producto (204, o 404 si no existe) | ADMIN |

Al arrancar se cargan 2 categorías (1 = Pisos, 2 = Baño) y 3 productos de prueba.

## Seguridad (Basic Auth)

Todo `/api/**` pide usuario y contraseña, salvo `/api/ping`. Los usuarios están en memoria (`config/SecurityConfig.java`) y las contraseñas se guardan con BCrypt:

| Usuario | Contraseña | Roles | Puede |
|---|---|---|---|
| `user` | `user123` | USER | Consultar (GET) |
| `admin` | `admin123` | USER, ADMIN | Consultar, crear, modificar y borrar |

```bash
curl -u user:user123 http://localhost:8083/api/productos
```

| Código | Significa |
|---|---|
| **401 Unauthorized** | No mandaste credenciales, o son incorrectas ("no sé quién sos") |
| **403 Forbidden** | Las credenciales están bien, pero tu rol no alcanza ("sé quién sos, pero no podés") |

> Basic Auth manda `usuario:contraseña` en Base64, que **no es cifrado**: solo se usa con HTTPS. En la clase 5 lo reemplazamos por JWT.

La consola web de H2 está desactivada desde esta clase (`spring.h2.console.enabled=false`).

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

### Credenciales

En cada request: pestaña **Authorization** → *Type:* **Basic Auth** → usuario y contraseña. Para no cargarlas una por una, configurarlas en la colección (clic en la colección → **Authorization**) y dejar los requests en *Inherit auth from parent*.

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
| **401 Unauthorized** | Falta la pestaña *Authorization* o la contraseña está mal |
| **403 Forbidden** | Usaste `user` para un POST, PUT o DELETE: hace falta `admin` |
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
